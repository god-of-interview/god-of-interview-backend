package com.capstone.godofinterview.domain.interview.dto.response;

import com.capstone.godofinterview.domain.interview.entity.Interview;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterviewStartResponse {

    private Long id;

    public static InterviewStartResponse toDto(Interview interview) {
        return new InterviewStartResponse(
            interview.getId()
        );
    }
}
