package com.capstone.godofinterview.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateQuestionRequest {

    @NotBlank(message = "질문 내용은 필수입니다.")
    private String content;
}
