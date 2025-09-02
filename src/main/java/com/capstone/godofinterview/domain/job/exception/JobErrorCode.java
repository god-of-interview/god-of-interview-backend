package com.capstone.godofinterview.domain.job.exception;

import org.springframework.http.HttpStatus;

import com.capstone.godofinterview.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobErrorCode implements ErrorCode {

    ALREADY_EXISTS_JOB_NAME("이미 존재하는 직업입니다.", HttpStatus.CONFLICT),
    JOB_NOT_FOUNT("직업을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_DELETED_JOB("이미 삭제된 직업입니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
