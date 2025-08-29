package com.capstone.godofinterview.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    private String nickname;

    @Size(max = 500, message = "자기소개는 200자 이내로 작성해주세요")
    private String bio;
}
