package com.capstone.godofinterview.domain.feedback.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpeechFeedbackResponse {

    private Integer score;                // 84점
    private Integer totalFillerWords;     // 6개
    private Map<String, Integer> fillerDetails; // {"음": 3, "어": 2, "그": 1}
    private String comment;               // "습관어 사용이 적절한 수준"
}
