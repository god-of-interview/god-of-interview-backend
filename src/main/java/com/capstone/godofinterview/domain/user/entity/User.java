package com.capstone.godofinterview.domain.user.entity;

import java.time.LocalDate;

import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임

    @Column(nullable = false, unique = true)
    private String email; // 로그인용 이메일

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String gender; // 성별

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String bio; // 자기소개

    @Column(nullable = false)
    private LocalDate birth; // 생년월일

    public User(String nickname, String email, String password, String gender, Role role, String bio, LocalDate birth) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.bio = bio;
        this.birth = birth;
    }
}
