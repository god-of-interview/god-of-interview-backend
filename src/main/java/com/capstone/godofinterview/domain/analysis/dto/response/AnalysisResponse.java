package com.capstone.godofinterview.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnalysisResponse {

    @JsonProperty("interview_id")
    private Long interviewId;

    private List<QuestionAnalysisResult> questions;
}
