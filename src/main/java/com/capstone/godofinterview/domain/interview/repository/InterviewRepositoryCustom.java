package com.capstone.godofinterview.domain.interview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewRecordResponse;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;

public interface InterviewRepositoryCustom {

    Page<InterviewRecordResponse> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, InterviewStatus status, Pageable pageable);
}
