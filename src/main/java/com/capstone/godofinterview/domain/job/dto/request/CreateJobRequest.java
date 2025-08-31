package com.capstone.godofinterview.domain.job.dto.request;

import com.capstone.godofinterview.domain.job.entity.JobCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateJobRequest {

    @NotNull(message = "직업 카테고리는 필수입니다.")
    private JobCategory jobCategory;

    @NotBlank(message = "직업명은 필수입니다.")
    private String name;
}
