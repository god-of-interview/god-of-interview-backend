package com.capstone.godofinterview.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.SpeechAnalysis;

public interface SpeechAnalysisRepository extends JpaRepository<SpeechAnalysis, Long> {
}
