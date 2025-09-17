package com.capstone.godofinterview.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GazeFeedbackResponse {

    private Integer score;                // 92점
    private Double attentionRatio;        // 87.5%
    private String comment;               // "카메라 응시율이 우수해요"
}
