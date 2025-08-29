package com.capstone.godofinterview.domain.user.dto.response;

import java.time.LocalDate;

import com.capstone.godofinterview.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

    private Long id;
    private String nickname;
    private String gender;
    private String bio;
    private LocalDate birth;

    public static UserResponse toDto(User user) {
        return new UserResponse(
            user.getId(),
            user.getNickname(),
            user.getGender(),
            user.getBio(),
            user.getBirth()
        );
    }
}
