package com.capstone.godofinterview.domain.interview.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.godofinterview.domain.analysis.service.AnalysisService;
import com.capstone.godofinterview.domain.interview.dto.response.InterviewStartResponse;
import com.capstone.godofinterview.domain.interview.dto.response.VideoUploadResponse;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;
import com.capstone.godofinterview.domain.interview.exception.InterviewErrorCode;
import com.capstone.godofinterview.domain.interview.exception.InterviewException;
import com.capstone.godofinterview.domain.interview.infra.s3.S3FileUpload;
import com.capstone.godofinterview.domain.interview.repository.InterviewRepository;
import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.service.JobService;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final UserService userService;
    private final JobService jobService;
    private final AnalysisService analysisService;
    private final S3FileUpload s3FileUpload;


    @Transactional
    @Override
    public InterviewStartResponse startInterview(Long userId, Long jobId) {

        User user = userService.getUser(userId);

        Job job = jobService.getJob(jobId);

        Interview interview = interviewRepository.save(
            new Interview(
                user,
                job,
                InterviewStatus.IN_PROGRESS
            )
        );

        return InterviewStartResponse.toDto(interview);
    }

    @Transactional(readOnly = true)
    @Override
    public Interview getInterview(Long interviewId) {

        Interview interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new InterviewException(InterviewErrorCode.INTERVIEW_NOT_FOUND));

        if (interview.getDeletedAt() != null) {
            throw new InterviewException(InterviewErrorCode.ALREADY_DELETED_INTERVIEW);
        }

        return interview;
    }

    @Override
    public VideoUploadResponse uploadVideo(Long interviewId, int questionNumber, MultipartFile video) {

        getInterview(interviewId);

        String videoUrl = null;
        try {
            videoUrl = s3FileUpload.uploadToS3(interviewId, questionNumber, video);
        } catch (Exception e) {
            throw new InterviewException(InterviewErrorCode.VIDEO_UPLOAD_FAILED);
        }

        return new VideoUploadResponse(
            interviewId,
            questionNumber,
            videoUrl
        );
    }

    @Transactional
    @Override
    public void completeInterview(Long interviewId) {
        Interview interview = getInterview(interviewId);

        interview.completeInterview();

        // 면접이 완료되면 S3에 있는 동영상 URL 5개 가져오기
        List<String> videoUrls = s3FileUpload.getInterviewVideoUrls(interviewId);
        if (videoUrls == null || videoUrls.isEmpty()) {
            throw new InterviewException(InterviewErrorCode.VIDEO_NOT_FOUND);
        }
        // 인터뷰 Id와 함께 동영상 URL 5개를 fastAPI 서버에 넘겨서 분석하기
        analysisService.startAnalysisAsync(interviewId, videoUrls);
    }
}
