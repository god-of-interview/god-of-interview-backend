package com.capstone.godofinterview.domain.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.domain.user.exception.UserErrorCode;
import com.capstone.godofinterview.domain.user.exception.UserException;
import com.capstone.godofinterview.domain.user.repository.UserRepository;
import com.capstone.godofinterview.global.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(Long userId) {

        User user = getUser(userId);

        return UserResponse.toDto(user);
    }

    @Override
    public User getUser(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 탈퇴한 유저인지 판단
        if (user.getDeletedAt() != null) {
            throw new UserException(UserErrorCode.ALREADY_DELETED_USER);
        }

        return user;
    }

    @Override
    public PageResponse<UserResponse> searchUsers(Pageable pageable, String keyword) {
        return new PageResponse<>(
            userRepository.searchUsers(pageable, keyword)
                .map(UserResponse::toDto)
        );
    }
}
