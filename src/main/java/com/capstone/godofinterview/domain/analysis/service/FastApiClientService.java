package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.capstone.godofinterview.domain.analysis.exception.AnalysisErrorCode;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FastApiClientService {

    private final RestClient restClient;

    public String analyzeInterview(Long interviewId, List<String> videoUrls) {
        try {
            Map<String, Object> request = Map.of(
                "interview_id", interviewId,
                "video_urls", videoUrls
            );

            return restClient.post()
                .uri("/analysis")
                .body(request)
                .retrieve()
                .body(String.class);

        } catch (ResourceAccessException e) {
            // 연결 타임아웃, 읽기 타임아웃 등
            throw new AnalysisException(AnalysisErrorCode.FASTAPI_CONNECTION_FAILED);
        } catch (HttpClientErrorException e) {
            // 4xx 에러 (잘못된 요청 등)
            throw new AnalysisException(AnalysisErrorCode.ANALYSIS_PROCESSING_FAILED);
        } catch (HttpServerErrorException e) {
            // 5xx 에러 (FastAPI 서버 내부 오류)
            throw new AnalysisException(AnalysisErrorCode.ANALYSIS_PROCESSING_FAILED);
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            throw new AnalysisException(AnalysisErrorCode.ANALYSIS_PROCESSING_FAILED);
        }
    }
}
