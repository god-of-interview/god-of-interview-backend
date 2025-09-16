package com.capstone.godofinterview.domain.interview.entity;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.user.entity.User;
import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Interview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    public Interview(User user, Job job, InterviewStatus status) {
        this.user = user;
        this.job = job;
        this.status = status;
    }

    public void completeInterview() {
        this.status = InterviewStatus.COMPLETED;
    }

    public void markAsAnalyzed() {
        this.status = InterviewStatus.ANALYZED;
    }
}


