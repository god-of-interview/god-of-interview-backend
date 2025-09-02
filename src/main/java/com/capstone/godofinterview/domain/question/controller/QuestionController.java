package com.capstone.godofinterview.domain.question.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.capstone.godofinterview.domain.question.service.QuestionService;
import com.capstone.godofinterview.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getRandomQuestions(
        @RequestParam Long jobId,
        @RequestParam(defaultValue = "5") int count
    ) {
        return ApiResponse.success(
            HttpStatus.OK,
            "면접 질문 조회가 완료되었습니다.",
            questionService.getRandomQuestions(jobId, count)
        );
    }
}
