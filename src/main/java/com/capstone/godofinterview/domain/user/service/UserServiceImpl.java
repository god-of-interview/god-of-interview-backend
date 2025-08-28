package com.capstone.godofinterview.domain.user.service;

import org.springframework.stereotype.Service;

import com.capstone.godofinterview.domain.user.dto.response.ProfileResponse;
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
    public ProfileResponse getMyProfile(Long userId) {

        User user = getUser(userId);

        return new ProfileResponse(
            user.getId(),
            user.getNickname(),
            user.getGender(),
            user.getBio(),
            user.getBirth()
        );
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
