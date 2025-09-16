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
public class FillerWordCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speech_analysis_id", nullable = false)
    private SpeechAnalysis speechAnalysis;

    @Column(nullable = false)
    private String word; // 습관어 ("음", "어", "그" 등)

    @Column(nullable = false)
    private Integer count; // 사용 횟수

    private FillerWordCount(SpeechAnalysis speechAnalysis, String word, Integer count) {
        this.speechAnalysis = speechAnalysis;
        this.word = word;
        this.count = count;
    }

    public static FillerWordCount of(SpeechAnalysis speechAnalysis, String word, Integer count) {
        return new FillerWordCount(
            speechAnalysis,
            word,
            count
        );
    }
}
