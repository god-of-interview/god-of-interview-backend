package com.capstone.godofinterview.domain.user.entity;

import java.util.Arrays;

import com.capstone.godofinterview.domain.user.exception.UserErrorCode;
import com.capstone.godofinterview.domain.user.exception.UserException;

public enum Role {
    USER, ADMIN;

    public static Role of(String role) {
        return Arrays.stream(Role.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new UserException(UserErrorCode.INVALID_USER_ROLE));
    }
}
