package com.capstone.godofinterview.domain.interview.dto.response;

import java.time.LocalDateTime;

import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterviewRecordResponse {

    private Long id;
    private String jobName;
    private InterviewStatus status;
    private LocalDateTime createdAt;

    public static InterviewRecordResponse from(Interview interview) {
        return new InterviewRecordResponse(
            interview.getId(),
            interview.getJob().getName(),
            interview.getStatus(),
            interview.getCreatedAt()
        );
    }
}
