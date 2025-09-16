package com.capstone.godofinterview.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.GazeAnalysis;

public interface GazeAnalysisRepository extends JpaRepository<GazeAnalysis, Long> {
}
