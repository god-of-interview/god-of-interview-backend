package com.capstone.godofinterview.domain.user.service;

import com.capstone.godofinterview.domain.user.dto.response.ProfileResponse;
import com.capstone.godofinterview.domain.user.entity.User;

public interface UserService {
    ProfileResponse getMyProfile(Long userId);

    User getUser(Long userId);
}
