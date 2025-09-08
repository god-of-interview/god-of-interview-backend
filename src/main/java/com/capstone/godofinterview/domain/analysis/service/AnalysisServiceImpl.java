package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;

import com.capstone.godofinterview.domain.analysis.dto.response.AnalysisResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.capstone.godofinterview.domain.analysis.exception.AnalysisException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    private final FastApiClientService fastApiClientService;

    @Async
    @Override
    public void startAnalysisAsync(Long interviewId, List<String> videoUrls) {
        try {
            log.info("AI 분석 시작: interviewId={}, videoCount={}", interviewId, videoUrls.size());

            AnalysisResponse analysisResponse = fastApiClientService.analyzeInterview(interviewId, videoUrls);

            log.info("분석 결과 = {}", analysisResponse);
            // TODO: 분석 결과를 DB에 저장
            log.info("AI 분석 완료: interviewId={}", interviewId);

        } catch (AnalysisException e) {
            log.error("AI 분석 실패: interviewId={}, error={}", interviewId, e.getMessage());
            // TODO: 분석 상태를 FAILED로 업데이트
        } catch (Exception e) {
            log.error("예상치 못한 분석 오류: interviewId={}", interviewId, e);
            // TODO: 분석 상태를 FAILED로 업데이트
        }
    }


}
