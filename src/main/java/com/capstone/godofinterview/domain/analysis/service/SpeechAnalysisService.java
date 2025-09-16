package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.analysis.dto.response.SpeechAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.FillerWordCount;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.SpeechAnalysis;
import com.capstone.godofinterview.domain.analysis.repository.FillerWordCountRepository;
import com.capstone.godofinterview.domain.analysis.repository.SpeechAnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpeechAnalysisService {

    private final SpeechAnalysisRepository speechAnalysisRepository;
    private final FillerWordCountRepository fillerWordCountRepository;

    @Transactional
    public void save(QuestionAnalysis questionAnalysis, SpeechAnalysisResult speechAnalysisResult) {

        // 1. 총 습관어 개수 계산
        int totalFillerWords = speechAnalysisResult.getFillerWordDetails().values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();

        // SpeechAnalysis 저장
        SpeechAnalysis speechAnalysis = SpeechAnalysis.of(questionAnalysis, totalFillerWords);
        SpeechAnalysis saved = speechAnalysisRepository.save(speechAnalysis);

        // 3. FillerWordCount들 배치 저장 (성능 개선)
        if (!speechAnalysisResult.getFillerWordDetails().isEmpty()) {
            List<FillerWordCount> fillerWordCounts = speechAnalysisResult.getFillerWordDetails()
                .entrySet()
                .stream()
                .map(entry -> FillerWordCount.of(saved, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

            fillerWordCountRepository.saveAll(fillerWordCounts);

        }
    }
}