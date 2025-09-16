package com.capstone.godofinterview.domain.analysis.entity;

import com.capstone.godofinterview.global.entity.BaseEntity;

import jakarta.persistence.Column;
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
public class GazeAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_analysis_id", nullable = false)
    private QuestionAnalysis questionAnalysis;

    @Column(nullable = false)
    private Double cameraAttentionRatio;

    private GazeAnalysis(QuestionAnalysis questionAnalysis, Double cameraAttentionRatio) {
        this.questionAnalysis = questionAnalysis;
        this.cameraAttentionRatio = cameraAttentionRatio;
    }

    public static GazeAnalysis of(QuestionAnalysis questionAnalysis, Double cameraAttentionRatio) {
        return new GazeAnalysis(
            questionAnalysis,
            cameraAttentionRatio
        );
    }
}
