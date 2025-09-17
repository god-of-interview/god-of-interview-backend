package com.capstone.godofinterview.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionFeedbackResponse {

    private Integer questionNumber;
    private String questionContent;
    private Integer questionScore;

    private EmotionFeedbackResponse emotionFeedbackResponse;
    private GazeFeedbackResponse gazeFeedbackResponse;
    private SpeechFeedbackResponse speechFeedbackResponse;

    // 질문별 종합 피드백
    private String positivePoints;            // "밝은 표정, 좋은 아이컨택"
    private String improvementPoints;         // "습관어 6회 사용"
    private String advice;                    // "답변 전 잠시 생각하는 시간을 가져보세요"
}
