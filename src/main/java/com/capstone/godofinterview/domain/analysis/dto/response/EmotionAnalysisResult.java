package com.capstone.godofinterview.domain.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmotionAnalysisResult {

    private Double angry;
    private Double disgust;
    private Double fear;
    private Double happy;
    private Double sad;
    private Double surprise;
    private Double neutral;
}
