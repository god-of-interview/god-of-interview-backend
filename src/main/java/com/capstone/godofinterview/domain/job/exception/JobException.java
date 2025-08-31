package com.capstone.godofinterview.domain.job.exception;

import com.capstone.godofinterview.global.exception.CustomException;
import com.capstone.godofinterview.global.exception.ErrorCode;

public class JobException extends CustomException {
    public JobException(ErrorCode errorCode) {
        super(errorCode);
    }
}
