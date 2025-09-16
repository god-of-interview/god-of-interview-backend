package com.capstone.godofinterview.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;

public interface QuestionAnalysisRepository extends JpaRepository<QuestionAnalysis, Long> {
}
