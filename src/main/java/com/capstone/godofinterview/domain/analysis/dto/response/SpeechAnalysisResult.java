package com.capstone.godofinterview.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpeechAnalysisResult {

    @JsonProperty("filler_word_details")
    private Map<String, Integer> fillerWordDetails;
}
