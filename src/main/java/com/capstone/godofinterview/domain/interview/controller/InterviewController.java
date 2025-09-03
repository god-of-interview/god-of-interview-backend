package com.capstone.godofinterview.domain.interview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewStartResponse;
import com.capstone.godofinterview.domain.interview.dto.response.VideoUploadResponse;
import com.capstone.godofinterview.domain.interview.service.InterviewService;
import com.capstone.godofinterview.global.jwt.Auth;
import com.capstone.godofinterview.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<InterviewStartResponse>> startInterview(
        @AuthenticationPrincipal Auth auth,
        @RequestParam Long jobId
    ) {
        return ApiResponse.success(
            HttpStatus.CREATED,
            "모의면접을 시작합니다.",
            interviewService.startInterview(auth.getUserId(), jobId)
        );
    }

    @PostMapping("/{interviewId}/upload")
    public ResponseEntity<ApiResponse<VideoUploadResponse>> uploadVideo(
        @PathVariable Long interviewId,
        @RequestParam int questionNumber,
        @RequestPart MultipartFile video
    ) {
        return ApiResponse.success(
            HttpStatus.OK,
            "면접 영상 업로드가 완료되었습니다.",
            interviewService.uploadVideo(interviewId, questionNumber, video)
        );
    }
}
