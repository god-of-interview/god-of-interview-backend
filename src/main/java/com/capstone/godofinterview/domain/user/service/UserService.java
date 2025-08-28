package com.capstone.godofinterview.domain.user.service;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.entity.User;

public interface UserService {
    UserResponse getProfile(Long userId);

    User getUser(Long userId);
}
