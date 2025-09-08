package com.capstone.godofinterview.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuestionAnalysisResult {

    @JsonProperty("question_number")
    private Long questionNumber;

    private EmotionAnalysisResult emotions;

    @JsonProperty("gaze_data")
    private GazeAnalysisResult gazeData;

    @JsonProperty("speech_data")
    private SpeechAnalysisResult speechData;
}