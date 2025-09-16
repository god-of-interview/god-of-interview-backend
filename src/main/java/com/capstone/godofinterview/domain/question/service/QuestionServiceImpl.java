package com.capstone.godofinterview.domain.question.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.service.JobService;
import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.capstone.godofinterview.domain.question.entity.Question;
import com.capstone.godofinterview.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final JobService jobService;

    @Transactional(readOnly = true)
    @Override
    public List<QuestionResponse> getRandomQuestions(Long jobId) {

        jobService.getJob(jobId);

        return questionRepository.findRandomQuestionsByJobId(jobId).stream()
            .map(QuestionResponse::toDto)
            .toList();
    }

    @Transactional
    @Override
    public QuestionResponse createQuestion(Long jobId, String content) {

        Job job = jobService.getJob(jobId);

        return QuestionResponse.toDto(
            questionRepository.save(
                new Question(
                    job,
                    content
                )
            )
        );
    }
}
