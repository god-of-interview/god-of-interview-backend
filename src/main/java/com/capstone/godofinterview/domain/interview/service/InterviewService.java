package com.capstone.godofinterview.domain.interview.service;

import org.springframework.web.multipart.MultipartFile;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewStartResponse;
import com.capstone.godofinterview.domain.interview.dto.response.VideoUploadResponse;
import com.capstone.godofinterview.domain.interview.entity.Interview;

public interface InterviewService {
    InterviewStartResponse startInterview(Long userId, Long jobId);

    Interview getInterview(Long interviewId);

    VideoUploadResponse uploadVideo(Long interviewId, int questionNumber, MultipartFile video);
}
