package com.capstone.godofinterview.domain.user.dto.response;

import java.time.LocalDate;

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
}
