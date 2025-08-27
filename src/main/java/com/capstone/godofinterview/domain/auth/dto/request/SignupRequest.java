package com.capstone.godofinterview.domain.auth.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank(message = "이메일은 필수로 입력해주세요.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[{\\]};:'\",<.>/?]).{8,}$",
        message = "비밀번호는 최소 8글자 이상, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야합니다."
    )
    private String password;

    @NotBlank(message = "사용자 닉네임은 필수로 입력해주세요.")
    private String nickname;

    @Pattern(
        regexp = "^(남자|여자)$",
        message = "성별은 '남자' 또는 '여자'만 입력 가능합니다"
    )
    private String gender;

    @Size(max = 500, message = "자기소개는 200자 이내로 작성해주세요")
    private String bio;

    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birth;
}
