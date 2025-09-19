package com.capstone.godofinterview.domain.job.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.job.dto.request.CreateJobRequest;
import com.capstone.godofinterview.domain.job.dto.response.JobCategoryResponse;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.entity.JobCategory;
import com.capstone.godofinterview.domain.job.exception.JobErrorCode;
import com.capstone.godofinterview.domain.job.exception.JobException;
import com.capstone.godofinterview.domain.job.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Transactional
    @Override
    public JobResponse createJob(CreateJobRequest request) {

        // 직업명 중복 체크
        if (jobRepository.existsByName(request.getName())) {
            throw new JobException(JobErrorCode.ALREADY_EXISTS_JOB_NAME);
        }

        Job savedJob = jobRepository.save(
            Job.create(
                request.getJobCategory(),
                request.getName()
            )
        );

        return JobResponse.toDto(savedJob);
    }

    @Override
    public List<JobCategoryResponse> getAllCategories() {
        return Arrays.stream(JobCategory.values())
            .map(JobCategoryResponse::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<JobResponse> getJobsByCategory(JobCategory jobCategory) {
        return jobRepository.findJobsByCategory(jobCategory).stream()
            .map(JobResponse::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Job getJob(Long id) {

        Job job = jobRepository.findById(id)
            .orElseThrow(() -> new JobException(JobErrorCode.JOB_NOT_FOUNT));

        if (job.getDeletedAt() != null) {
            throw new JobException(JobErrorCode.ALREADY_DELETED_JOB);
        }

        return job;
    }
}
