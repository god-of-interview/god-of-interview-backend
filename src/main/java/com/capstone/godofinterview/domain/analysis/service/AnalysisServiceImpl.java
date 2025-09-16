package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;

import com.capstone.godofinterview.domain.analysis.dto.response.AnalysisResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    private final FastApiClientService fastApiClientService;
    private final AnalysisDataService analysisDataService;

    @Async
    @Override
    @Transactional
    public void startAnalysisAsync(Long interviewId, List<String> videoUrls) {
        try {
            log.info("🚀 AI 분석 시작: interviewId={}, videoCount={}", interviewId, videoUrls.size());

            // 1. FastAPI 분석 수행
            AnalysisResponse analysisResponse = fastApiClientService.analyzeInterview(interviewId, videoUrls);

            // 2. 분석 결과 저장 (AnalysisDataService에 위임)
            analysisDataService.saveAnalysisResult(interviewId, analysisResponse);

            log.info("✅ AI 분석 완료: interviewId={}", interviewId);

        } catch (Exception e) {
            analysisDataService.markAnalysisAsFailed(interviewId, e.getMessage());
        }
    }
}
