package com.capstone.godofinterview.domain.question.repository;

import java.util.List;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;

public interface QuestionRepositoryCustom {

    List<QuestionResponse> findRandomQuestionsByJobId(Long jobId);
}
