package com.capstone.godofinterview.domain.analysis.exception;

import org.springframework.http.HttpStatus;

import com.capstone.godofinterview.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalysisErrorCode implements ErrorCode {

    FASTAPI_CONNECTION_FAILED("FastAPI 서버 연결에 실패했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    ANALYSIS_PROCESSING_FAILED("AI 분석 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;
}
