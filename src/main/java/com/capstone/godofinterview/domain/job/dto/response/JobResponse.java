package com.capstone.godofinterview.domain.job.dto.response;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.entity.JobCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JobResponse {

    private Long id;
    private JobCategory jobCategory;
    private String name;

    public static JobResponse toDto(Job job) {
        return new JobResponse(
            job.getId(),
            job.getJobCategory(),
            job.getName()
        );
    }
}
