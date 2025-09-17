package com.capstone.godofinterview.domain.interview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("""
        SELECT i FROM Interview i 
        JOIN i.job WHERE i.user.id = :userId 
        AND i.deletedAt IS NULL 
        AND i.status = :status 
        ORDER BY i.createdAt DESC
        """)
    Page<Interview> findByUserIdAndStatusOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("status") InterviewStatus status, Pageable pageable);
}
