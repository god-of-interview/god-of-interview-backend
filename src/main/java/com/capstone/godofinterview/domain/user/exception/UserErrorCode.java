package com.capstone.godofinterview.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.capstone.godofinterview.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    INVALID_USER_ROLE("유효하지 않은 유저 권한입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXISTS_EMAIL("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    ALREADY_EXISTS_NICKNAME("이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;

}
