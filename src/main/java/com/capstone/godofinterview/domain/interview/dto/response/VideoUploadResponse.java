package com.capstone.godofinterview.domain.interview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VideoUploadResponse {

    private Long interviewId;
    private int questionNumber;
    private String videoUrl;
}
