package com.capstone.godofinterview.domain.job.dto.response;

import com.capstone.godofinterview.domain.job.entity.JobCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JobCategoryResponse {

    private JobCategory jobCategory;
    private String displayName;

    public static JobCategoryResponse toDto(JobCategory jobCategory) {
        return new JobCategoryResponse(
            jobCategory,
            jobCategory.getDisplayName()
        );
    }
}
