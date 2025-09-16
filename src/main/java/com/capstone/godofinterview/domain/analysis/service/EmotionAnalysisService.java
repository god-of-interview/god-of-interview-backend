package com.capstone.godofinterview.domain.analysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.analysis.dto.response.EmotionAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.EmotionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.repository.EmotionAnalysisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {

    private final EmotionAnalysisRepository emotionAnalysisRepository;

    @Transactional
    public void save(QuestionAnalysis questionAnalysis, EmotionAnalysisResult emotions) {

        EmotionAnalysis emotionAnalysis = EmotionAnalysis.of(
            questionAnalysis,
            emotions.getAngry(),
            emotions.getDisgust(),
            emotions.getFear(),
            emotions.getHappy(),
            emotions.getSad(),
            emotions.getSurprise(),
            emotions.getNeutral()
        );
    }
}
