package com.capstone.godofinterview.domain.job.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.job.dto.request.CreateJobRequest;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.Job;
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
            new Job(
                request.getJobCategory(),
                request.getName()
            )
        );

        return JobResponse.toDto(savedJob);
    }
}
