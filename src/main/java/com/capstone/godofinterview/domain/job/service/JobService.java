package com.capstone.godofinterview.domain.job.service;

import com.capstone.godofinterview.domain.job.dto.request.CreateJobRequest;
import com.capstone.godofinterview.domain.job.dto.response.JobResponse;

public interface JobService {

    JobResponse createJob(CreateJobRequest request);
}
