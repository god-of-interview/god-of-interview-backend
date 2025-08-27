package com.capstone.godofinterview.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.godofinterview.domain.auth.dto.LoginResponse;
import com.capstone.godofinterview.domain.auth.dto.request.LoginRequest;
import com.capstone.godofinterview.domain.auth.dto.request.SignupRequest;
import com.capstone.godofinterview.domain.auth.dto.response.SignupResponse;
import com.capstone.godofinterview.domain.user.entity.Role;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.domain.user.exception.UserErrorCode;
import com.capstone.godofinterview.domain.user.exception.UserException;
import com.capstone.godofinterview.domain.user.repository.UserRepository;
import com.capstone.godofinterview.global.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public SignupResponse signup(SignupRequest request) {

        // 이메일 중복 체크
        checkEmailExists(request);

        // 닉네임 중복 체크
        checkNicknameExists(request);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 유저 객체 생성
        User user = new User(
            request.getNickname(),
            request.getEmail(),
            encodedPassword,
            request.getGender(),
            Role.USER,
            request.getBio(),
            request.getBirth()
        );

        // 유저 DB 저장
        User savedUser = userRepository.save(user);

        // 토큰 생성
        String fullToken = jwtUtil.createToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getNickname(),
            savedUser.getRole()
        );

        // 토큰 앞에 접두사 제거
        String accessToken = jwtUtil.substringToken(fullToken);

        // 회원가입 시 토큰 반환(바로 로그인)
        return new SignupResponse(accessToken);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // 이메일로 유저 찾기
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 요청한 비밀번호와 암호화된 비밀번호 비교하기
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성하기
        String fullToken = jwtUtil.createToken(
            user.getId(),
            user.getEmail(),
            user.getNickname(),
            user.getRole()
        );

        // 토큰 접두사 제거
        String accessToken = jwtUtil.substringToken(fullToken);

        return new LoginResponse(accessToken);
    }

    // ====================== 헬퍼메서드 ======================
    private void checkEmailExists(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(UserErrorCode.ALREADY_EXISTS_EMAIL);
        }
    }

    private void checkNicknameExists(SignupRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
        }
    }
}
