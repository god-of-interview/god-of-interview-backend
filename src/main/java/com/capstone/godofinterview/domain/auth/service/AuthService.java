package com.capstone.godofinterview.domain.auth.service;

import com.capstone.godofinterview.domain.auth.dto.SignupRequest;
import com.capstone.godofinterview.domain.auth.dto.SignupResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest request);
}
