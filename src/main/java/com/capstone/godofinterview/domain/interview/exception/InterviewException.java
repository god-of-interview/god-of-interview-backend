package com.capstone.godofinterview.domain.interview.exception;

import com.capstone.godofinterview.global.exception.CustomException;
import com.capstone.godofinterview.global.exception.ErrorCode;

public class InterviewException extends CustomException {
    public InterviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
