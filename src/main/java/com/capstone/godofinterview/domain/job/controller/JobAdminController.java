package com.capstone.godofinterview.domain.job.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.job.dto.request.CreateJobRequest;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.service.JobService;
import com.capstone.godofinterview.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/jobs")
public class JobAdminController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
        @Valid @RequestBody CreateJobRequest request) {
        return ApiResponse.success(
            HttpStatus.CREATED,
            "직업이 성공적으로 등록되었습니다.",
            jobService.createJob(request)
        );
    }
}
