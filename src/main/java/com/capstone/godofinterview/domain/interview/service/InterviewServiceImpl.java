package com.capstone.godofinterview.domain.interview.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewStartResponse;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;
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
}
