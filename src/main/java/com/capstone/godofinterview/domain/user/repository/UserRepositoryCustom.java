package com.capstone.godofinterview.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;

public interface UserRepositoryCustom {

    Page<UserResponse> searchUsers(Pageable pageable, String keyword);
}
