package com.capstone.godofinterview.domain.question.dto.response;

import com.capstone.godofinterview.domain.question.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionResponse {

    private Long id;
    private String content;

    public static QuestionResponse toDto(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getContent()
        );
    }
}
