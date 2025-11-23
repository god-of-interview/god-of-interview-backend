package com.capstone.godofinterview.domain.job.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.exception.JobErrorCode;
import com.capstone.godofinterview.domain.job.exception.JobException;
import com.capstone.godofinterview.domain.job.repository.JobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Job 캐싱 전담 서비스
 * - 프록시 기반 캐싱을 위해 별도 서비스로 분리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobCacheService {

    private final JobRepository jobRepository;

    /**
     * Job 단건 조회 (캐싱)
     * - 캐시 키: jobId
     * - 캐시 만료: 1시간
     */
    @Cacheable(value = "jobs", key = "#id")
    @Transactional(readOnly = true)
    public Job getJob(Long id) {
        log.info("DB 조회 실행 - jobId: {} (캐시 미스!)", id);

        Job job = jobRepository.findById(id)
            .orElseThrow(() -> new JobException(JobErrorCode.JOB_NOT_FOUNT));

        if (job.getDeletedAt() != null) {
            throw new JobException(JobErrorCode.ALREADY_DELETED_JOB);
        }

        return job;
    }

    /**
     * Job 캐시 무효화
     * - Job 생성/수정/삭제 시 호출
     */
    @CacheEvict(value = "jobs", key = "#jobId")
    public void evictCache(Long jobId) {
        log.info("캐시 삭제 - jobId: {}", jobId);
    }
}
