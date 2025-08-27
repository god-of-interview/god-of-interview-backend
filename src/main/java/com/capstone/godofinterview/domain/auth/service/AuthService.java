package com.capstone.godofinterview.domain.auth.service;

import com.capstone.godofinterview.domain.auth.dto.response.LoginResponse;
import com.capstone.godofinterview.domain.auth.dto.request.LoginRequest;
import com.capstone.godofinterview.domain.auth.dto.request.SignupRequest;
import com.capstone.godofinterview.domain.auth.dto.response.SignupResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);
}
