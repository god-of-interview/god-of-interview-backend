package com.capstone.godofinterview.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.analysis.entity.FillerWordCount;

public interface FillerWordCountRepository extends JpaRepository<FillerWordCount, Long> {
}
