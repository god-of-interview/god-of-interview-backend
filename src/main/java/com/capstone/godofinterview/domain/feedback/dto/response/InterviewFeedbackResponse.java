package com.capstone.godofinterview.domain.feedback.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InterviewFeedbackResponse {

    // 면접에 대한 기본 정보들
    private Long interviewId;
    private String jobName;
    private LocalDateTime interviewDate;

    // 종합 피드백
    private OverAllFeedbackResponse overAllFeedbackResponse;

    private List<QuestionFeedbackResponse> questionFeedbackResponseList;
}
