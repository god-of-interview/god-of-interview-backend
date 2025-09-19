package com.capstone.godofinterview.domain.job.entity;

import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobCategory jobCategory;

    @Column(nullable = false, unique = true)
    private String name;

    private Job(JobCategory jobCategory, String name) {
        this.jobCategory = jobCategory;
        this.name = name;
    }

    public static Job create(JobCategory jobCategory, String name) {
        return new Job(
            jobCategory,
            name
        );
    }
}
