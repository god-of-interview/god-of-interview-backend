package com.capstone.godofinterview.domain.analysis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;

public interface InterviewAnalysisRepository extends JpaRepository<InterviewAnalysis, Long> {

    Optional<InterviewAnalysis> findByInterviewId(Long interviewId);

    boolean existsByInterviewId(Long interviewId);

    @Query("""
        SELECT DISTINCT ia FROM InterviewAnalysis ia
        JOIN FETCH ia.questionAnalyses qa 
        LEFT JOIN FETCH qa.emotionAnalysis 
        LEFT JOIN FETCH qa.gazeAnalysis 
        LEFT JOIN FETCH qa.speechAnalysis sa 
        LEFT JOIN FETCH sa.fillerWordCounts 
        WHERE ia.interview.id = :interviewId 
        ORDER BY qa.questionNumber ASC
        """)
    Optional<InterviewAnalysis> findByInterviewIdWithAllAnalysis(@Param("interviewId") Long interviewId);
}
