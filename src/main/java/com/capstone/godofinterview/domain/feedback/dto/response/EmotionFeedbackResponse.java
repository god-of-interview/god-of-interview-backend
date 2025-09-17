package com.capstone.godofinterview.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmotionFeedbackResponse {

    private Integer score;                // 85점
    private String dominantEmotion;       // "긍정적"
    private Double happyRatio;            // 45.7%
    private Double neutralRatio;          // 26.8%
    private String comment;               // "밝고 자연스러운 표정"
}
