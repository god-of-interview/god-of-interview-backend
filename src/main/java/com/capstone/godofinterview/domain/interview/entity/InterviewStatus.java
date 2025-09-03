package com.capstone.godofinterview.domain.interview.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterviewStatus {

    IN_PROGRESS("면접 진행 중"),
    COMPLETED("면접 완료"),
    ANALYZED("분석 완료");

    private final String description;
}
