package com.capstone.godofinterview.domain.question.service;

import java.util.List;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;

public interface QuestionService {

    List<QuestionResponse> getRandomQuestion(Long jobId);

    List<QuestionResponse> getRandomQuestions(Long jobId);

    QuestionResponse createQuestion(Long jobId, String content);
}
