package com.capstone.godofinterview.domain.user.exception;

import com.capstone.godofinterview.global.exception.CustomException;
import com.capstone.godofinterview.global.exception.ErrorCode;

public class UserException extends CustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
