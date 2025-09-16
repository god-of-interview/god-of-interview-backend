package com.capstone.godofinterview.domain.analysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.analysis.dto.response.GazeAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.GazeAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.repository.GazeAnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GazeAnalysisService {

    private final GazeAnalysisRepository gazeAnalysisRepository;

    @Transactional
    public void save(QuestionAnalysis questionAnalysis, GazeAnalysisResult gazeData) {

        GazeAnalysis gazeAnalysis = GazeAnalysis.of(
            questionAnalysis,
            gazeData.getCameraAttentionRatio()
        );

        gazeAnalysisRepository.save(gazeAnalysis);
    }
}

