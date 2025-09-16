package com.capstone.godofinterview.domain.analysis.entity;

import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class QuestionAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_analysis_id", nullable = false)
    private InterviewAnalysis interviewAnalysis;

    @Column(nullable = false)
    private Integer questionNumber;

    private QuestionAnalysis(InterviewAnalysis interviewAnalysis, Integer questionNumber) {
        this.interviewAnalysis = interviewAnalysis;
        this.questionNumber = questionNumber;
    }

    public static QuestionAnalysis of(InterviewAnalysis interviewAnalysis, Integer questionNumber) {
        return new QuestionAnalysis(
            interviewAnalysis,
            questionNumber
        );
    }
}
