package com.capstone.godofinterview.domain.question.service;

import java.util.List;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;

public interface QuestionService {

    List<QuestionResponse> getRandomQuestions(Long jobId, int count);

    QuestionResponse createQuestion(Long jobId, String content);
}
