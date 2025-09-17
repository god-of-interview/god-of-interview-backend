package com.capstone.godofinterview.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OverAllFeedbackResponse {

    private Integer totalScore;
    private String grade;
    private String overAllComment;

    private Integer emotionScore;
    private Integer gazeScore;
    private Integer speechScore;

    private String strengths;
    private String improvements;
}
