package com.capstone.godofinterview.domain.feedback.exception;

import com.capstone.godofinterview.global.exception.CustomException;
import com.capstone.godofinterview.global.exception.ErrorCode;

public class FeedbackException extends CustomException {
    public FeedbackException(ErrorCode errorCode) {
        super(errorCode);
    }
}
