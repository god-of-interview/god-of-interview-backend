package com.capstone.godofinterview.domain.analysis.exception;

import com.capstone.godofinterview.global.exception.CustomException;
import com.capstone.godofinterview.global.exception.ErrorCode;

public class AnalysisException extends CustomException {
    public AnalysisException(ErrorCode errorCode) {
        super(errorCode);
    }
}
