package com.capstone.godofinterview.domain.feedback.exception;

import org.springframework.http.HttpStatus;

import com.capstone.godofinterview.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {

    ANALYSIS_NOT_FOUND("면접 분석 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_FEEDBACK_ACCESS("해당 면접에 대한 피드백 조회 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FEEDBACK_GENERATION_FAILED("피드백 생성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

}
