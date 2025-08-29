package com.capstone.godofinterview.domain.user.service;

import org.springframework.data.domain.Pageable;

import com.capstone.godofinterview.domain.user.dto.request.UpdateProfileRequest;
import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.global.response.PageResponse;

public interface UserService {
    UserResponse getProfile(Long userId);

    User getUser(Long userId);

    PageResponse<UserResponse> searchUsers(Pageable pageable, String keyword);

    void updateProfile(Long userId, UpdateProfileRequest request);

    void deleteMyAccount(Long userId, String password);
}
