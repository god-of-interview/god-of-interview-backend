package com.capstone.godofinterview.global.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capstone.godofinterview.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 커스텀 오류로 발생한 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Void>> handleBusinessException(CustomException e) {
        return ApiResponse.error(e.getErrorCode().getStatus(), e.getMessage());
    }

    // validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse("잘못된 요청입니다.");

        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage);
    }

    // 2. 그 외 모든 예외 처리 (최후의 보루)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내에서 오류가 발생하였습니다.");
    }
}
