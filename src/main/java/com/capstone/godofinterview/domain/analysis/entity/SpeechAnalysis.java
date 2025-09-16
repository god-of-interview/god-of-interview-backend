package com.capstone.godofinterview.domain.analysis.entity;

import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SpeechAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_analysis_id", nullable = false)
    private QuestionAnalysis questionAnalysis;

    private SpeechAnalysis(QuestionAnalysis questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public static SpeechAnalysis from(QuestionAnalysis questionAnalysis) {
        return new SpeechAnalysis(
            questionAnalysis
        );
    }
}
