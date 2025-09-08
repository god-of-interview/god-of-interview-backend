package com.capstone.godofinterview.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GazeAnalysisResult {

    @JsonProperty("camera_attention_ratio")
    private Double cameraAttentionRatio;
}
