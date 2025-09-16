package com.capstone.godofinterview.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.EmotionAnalysis;

public interface EmotionAnalysisRepository extends JpaRepository<EmotionAnalysis, Long> {
}
