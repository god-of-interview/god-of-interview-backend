package com.capstone.godofinterview.global.jwt;

import com.capstone.godofinterview.domain.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Auth {

    private final Long userId;

    private final Role userRole;
}
