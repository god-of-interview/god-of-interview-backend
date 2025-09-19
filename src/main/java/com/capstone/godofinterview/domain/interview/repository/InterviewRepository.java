package com.capstone.godofinterview.domain.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.interview.entity.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long>, InterviewRepositoryCustom {

    // @Query("""
    //     SELECT i FROM Interview i
    //     JOIN i.job WHERE i.user.id = :userId
    //     AND i.deletedAt IS NULL
    //     AND i.status = :status
    //     ORDER BY i.createdAt DESC
    //     """)
    // Page<Interview> findByUserIdAndStatusOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("status") InterviewStatus status, Pageable pageable);
}
