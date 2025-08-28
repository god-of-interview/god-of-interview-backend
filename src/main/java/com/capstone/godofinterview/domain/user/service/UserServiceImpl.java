package com.capstone.godofinterview.domain.user.service;

import org.springframework.stereotype.Service;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.domain.user.exception.UserErrorCode;
import com.capstone.godofinterview.domain.user.exception.UserException;
import com.capstone.godofinterview.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(Long userId) {

        User user = getUser(userId);

        return new UserResponse(
            user.getId(),
            user.getNickname(),
            user.getGender(),
            user.getBio(),
            user.getBirth()
        );
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
}
