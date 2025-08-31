package com.capstone.godofinterview.domain.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.godofinterview.domain.user.dto.request.DeleteAccountRequest;
import com.capstone.godofinterview.domain.user.dto.request.UpdateProfileRequest;
import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.capstone.godofinterview.domain.user.service.UserService;
import com.capstone.godofinterview.global.jwt.Auth;
import com.capstone.godofinterview.global.response.ApiResponse;
import com.capstone.godofinterview.global.response.PageResponse;

import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> searchUsers(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(defaultValue = "") String keyword
    ) {
        return ApiResponse.success(
            HttpStatus.OK,
            "유저 검색이 완료되었습니다.",
            userService.searchUsers(pageable, keyword)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
        @AuthenticationPrincipal Auth auth,
        @Valid @RequestBody UpdateProfileRequest request
    ) {

        userService.updateProfile(auth.getUserId(), request);

        return ApiResponse.success(
            HttpStatus.OK,
            "회원 정보가 수정되었습니다.",
            null
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(
        @AuthenticationPrincipal Auth auth,
        @RequestBody DeleteAccountRequest request
    ) {

        userService.deleteMyAccount(auth.getUserId(), request.getPassword());

        return ApiResponse.success(
            HttpStatus.OK,
            "회원 탈퇴가 완료되었습니다.",
            null
        );
    }
}
