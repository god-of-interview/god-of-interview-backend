package com.capstone.godofinterview.domain.job.repository;

import java.util.List;

import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.JobCategory;

public interface JobRepositoryCustom {

    List<JobResponse> findJobsByCategory(JobCategory jobCategory);
}
