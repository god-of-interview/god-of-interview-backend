package com.capstone.godofinterview.domain.job.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.job.dto.response.JobCategoryResponse;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.JobCategory;
import com.capstone.godofinterview.domain.job.service.JobService;
import com.capstone.godofinterview.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<JobCategoryResponse>>> getAllCategories() {
        return ApiResponse.success(
            HttpStatus.OK,
            "카테고리 전체 조회가 완료되었습니다.",
            jobService.getAllCategories()
        );
    }

    @GetMapping("/categories/{jobCategory}")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobsByCategory(@PathVariable JobCategory jobCategory) {
        return ApiResponse.success(
            HttpStatus.OK,
            "직업 목록 조회가 완료되었습니다.",
            jobService.getJobsByCategory(jobCategory)
        );
    }
}

