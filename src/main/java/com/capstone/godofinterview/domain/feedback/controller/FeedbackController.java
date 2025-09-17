package com.capstone.godofinterview.domain.feedback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.feedback.dto.response.InterviewFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.service.FeedbackService;
import com.capstone.godofinterview.global.jwt.Auth;
import com.capstone.godofinterview.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/interviews/{interviewId}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> getInterviewFeedback(
        @AuthenticationPrincipal Auth auth,
        @PathVariable Long interviewId
    ) {
        return ApiResponse.success(
            HttpStatus.OK,
            "면접 피드백 조회가 완료되었습니다.",
            feedbackService.getInterviewFeedback(interviewId, auth.getUserId())
        );
    }

}
