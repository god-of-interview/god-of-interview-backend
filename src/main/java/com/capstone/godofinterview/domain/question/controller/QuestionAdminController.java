package com.capstone.godofinterview.domain.question.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.question.dto.request.CreateQuestionRequest;
import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.capstone.godofinterview.domain.question.service.QuestionService;
import com.capstone.godofinterview.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class QuestionAdminController {

    private final QuestionService questionService;

    @PostMapping("/jobs/{jobId}/questions")
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
        @PathVariable Long jobId,
        @Valid @RequestBody CreateQuestionRequest request
    ) {
        return ApiResponse.success(
            HttpStatus.CREATED,
            "질문 생성이 완료되었습니다.",
            questionService.createQuestion(jobId, request.getContent())
        );
    }
}
