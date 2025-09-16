package com.capstone.godofinterview.domain.analysis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;

public interface InterviewAnalysisRepository extends JpaRepository<InterviewAnalysis, Long> {

    Optional<InterviewAnalysis> findByInterviewId(Long interviewId);
}
