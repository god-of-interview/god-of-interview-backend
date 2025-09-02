package com.capstone.godofinterview.domain.job.service;

import java.util.List;

import com.capstone.godofinterview.domain.job.dto.request.CreateJobRequest;
import com.capstone.godofinterview.domain.job.dto.response.JobCategoryResponse;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.entity.JobCategory;

public interface JobService {

    JobResponse createJob(CreateJobRequest request);

    List<JobCategoryResponse> getAllCategories();

    List<JobResponse> getJobsByCategory(JobCategory jobCategory);

    Job getJob(Long id);
}
