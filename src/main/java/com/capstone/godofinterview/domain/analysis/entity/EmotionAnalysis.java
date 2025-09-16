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
public class EmotionAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_analysis_id", nullable = false)
    private QuestionAnalysis questionAnalysis;

    @Column(nullable = false)
    private Double angry;

    @Column(nullable = false)
    private Double disgust;

    @Column(nullable = false)
    private Double fear;

    @Column(nullable = false)
    private Double happy;

    @Column(nullable = false)
    private Double sad;

    @Column(nullable = false)
    private Double surprise;

    @Column(nullable = false)
    private Double neutral;

    private EmotionAnalysis(QuestionAnalysis questionAnalysis, Double angry, Double disgust, Double fear, Double happy,
        Double sad, Double surprise, Double neutral) {
        this.questionAnalysis = questionAnalysis;
        this.angry = angry;
        this.disgust = disgust;
        this.fear = fear;
        this.happy = happy;
        this.sad = sad;
        this.surprise = surprise;
        this.neutral = neutral;
    }

    public static EmotionAnalysis of(QuestionAnalysis questionAnalysis, Double angry, Double disgust, Double fear, Double happy,
        Double sad, Double surprise, Double neutral) {
        return new EmotionAnalysis(
            questionAnalysis,
            angry,
            disgust,
            fear,
            happy,
            sad,
            surprise,
            neutral
        );
    }
}
