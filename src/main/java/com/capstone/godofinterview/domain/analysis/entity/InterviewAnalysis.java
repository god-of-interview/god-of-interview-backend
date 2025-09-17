package com.capstone.godofinterview.domain.analysis.entity;

import java.util.ArrayList;
import java.util.List;

import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class InterviewAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @OneToMany(mappedBy = "interviewAnalysis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionAnalysis> questionAnalyses = new ArrayList<>();

    private InterviewAnalysis(Interview interview) {
        this.interview = interview;
    }

    public static InterviewAnalysis from(Interview interview) {
        return new InterviewAnalysis(
            interview
        );
    }
}
