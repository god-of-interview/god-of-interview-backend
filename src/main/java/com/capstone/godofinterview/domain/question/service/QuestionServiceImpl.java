package com.capstone.godofinterview.domain.question.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.service.JobService;
import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.capstone.godofinterview.domain.question.entity.Question;
import com.capstone.godofinterview.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final JobService jobService;
    private final QuestionCacheService cacheService;

    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestion(Long jobId) {

        jobService.getJob(jobId);

        return questionRepository.findRandomQuestionsByJobId(jobId);
    }

    /**
     * 랜덤 질문 5개 조회
     * 캐시된 전체 질문에서 랜덤 선택
     */
    @Transactional(readOnly = true)
    @Override
    public List<QuestionResponse> getRandomQuestions(Long jobId) {
        jobService.getJob(jobId);
        List<QuestionResponse> allQuestions = cacheService.getAllQuestionsByJob(jobId);

        if (allQuestions.size() <= 5) {
            return allQuestions;
        }

        // 랜덤 인덱스 5개 선택 (셔플 없이!)
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<Integer> selectedIndexes = new HashSet<>();

        while (selectedIndexes.size() < 5) {
            selectedIndexes.add(random.nextInt(allQuestions.size()));
        }

        return selectedIndexes.stream()
            .map(allQuestions::get)
            .collect(Collectors.toList());
    }

    /**
     * 질문 생성 (캐시 무효화)
     * 해당 jobId의 캐시만 삭제
     */
    @Transactional
    @Override
    public QuestionResponse createQuestion(Long jobId, String content) {
        Job job = jobService.getJob(jobId);

        QuestionResponse response = QuestionResponse.toDto(
            questionRepository.save(
                new Question(
                    job,
                    content
                )
            )
        );

        // 캐시 무효화
        cacheService.evictCache(jobId);

        return response;
    }
}
