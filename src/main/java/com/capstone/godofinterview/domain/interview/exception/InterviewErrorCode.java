package com.capstone.godofinterview.domain.interview.exception;

import org.springframework.http.HttpStatus;

import com.capstone.godofinterview.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterviewErrorCode implements ErrorCode {

    INTERVIEW_NOT_FOUND("면접 기록을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ALREADY_DELETED_INTERVIEW("이미 삭제된 면접 기록입니다.", HttpStatus.NOT_FOUND),
    VIDEO_UPLOAD_FAILED("동영상 업로드에 실패하였습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
