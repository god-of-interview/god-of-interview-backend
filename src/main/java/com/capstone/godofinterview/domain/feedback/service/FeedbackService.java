package com.capstone.godofinterview.domain.feedback.service;

import com.capstone.godofinterview.domain.feedback.dto.response.InterviewFeedbackResponse;

public interface FeedbackService {

    InterviewFeedbackResponse getInterviewFeedback(Long interviewId, Long userId);
}
