package com.capstone.godofinterview.domain.question.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.capstone.godofinterview.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 질문 캐싱 전담 서비스
 * - 프록시 기반 캐싱을 위해 별도 서비스로 분리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionCacheService {

    private final QuestionRepository questionRepository;

    /**
     * 직무별 전체 질문 조회 (캐싱)
     * - 캐시 키: jobId
     * - 캐시 만료: 1시간
     */
    @Cacheable(value = "questions", key = "#jobId")
    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestionsByJob(Long jobId) {
        log.info("DB 조회 실행 - jobId: {} (캐시 미스!)", jobId);
        return questionRepository.findAllByJobIdAndDeletedAtIsNull(jobId);
    }

    /**
     * 질문 캐시 무효화
     * - 질문 생성/수정/삭제 시 호출
     */
    @CacheEvict(value = "questions", key = "#jobId")
    public void evictCache(Long jobId) {
        log.info("캐시 삭제 - jobId: {}", jobId);
    }
}