package com.capstone.godofinterview.domain.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.user.dto.request.UpdateProfileRequest;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserResponse getProfile(Long userId) {

        User user = getUser(userId);

        return UserResponse.toDto(user);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public PageResponse<UserResponse> searchUsers(Pageable pageable, String keyword) {
        return new PageResponse<>(userRepository.searchUsers(pageable, keyword));
    }

    @Transactional
    @Override
    public void updateProfile(Long userId, UpdateProfileRequest request) {

        User user = getUser(userId);

        // 닉네임이 null이 아니고, 동일한 닉네임이 아닐때, 다른 사람들과 중복된 닉네임 체크
        if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
            if (!user.getNickname().equals(request.getNickname()) && userRepository.existsByNickname(request.getNickname())) {
                throw new UserException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
            }
        }

        user.updateProfile(request);
    }

    @Transactional
    @Override
    public void deleteMyAccount(Long userId, String password) {

        User user = getUser(userId);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        user.delete();
    }
}
