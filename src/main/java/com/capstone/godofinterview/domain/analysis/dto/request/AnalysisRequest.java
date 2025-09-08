package com.capstone.godofinterview.domain.analysis.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AnalysisRequest {

    @JsonProperty("interview_id")
    private Long interviewId;

    @JsonProperty("video_urls")
    private List<String> videoUrls;
}
