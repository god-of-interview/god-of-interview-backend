package com.capstone.godofinterview.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.service.UserService;
import com.capstone.godofinterview.global.jwt.Auth;
import com.capstone.godofinterview.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(@AuthenticationPrincipal Auth auth) {
        return ApiResponse.success(
            HttpStatus.OK,
            "나의 프로필 조회가 완료되었습니다.",
            userService.getProfile(auth.getUserId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@PathVariable Long id) {
        return ApiResponse.success(
            HttpStatus.OK,
            "프로필 조회가 완료되었습니다. id : " + id,
            userService.getProfile(id)
        );
    }
}
