package com.capstone.godofinterview.domain.interview.service;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewStartResponse;

public interface InterviewService {
    InterviewStartResponse startInterview(Long userId, Long jobId);
}
