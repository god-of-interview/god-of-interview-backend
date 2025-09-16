package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;

import com.capstone.godofinterview.domain.analysis.dto.response.AnalysisResponse;
import com.capstone.godofinterview.domain.analysis.dto.response.QuestionAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisErrorCode;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisException;
import com.capstone.godofinterview.domain.analysis.repository.InterviewAnalysisRepository;
import com.capstone.godofinterview.domain.analysis.repository.QuestionAnalysisRepository;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.repository.InterviewRepository;
import com.capstone.godofinterview.domain.interview.service.InterviewService;

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
    private final InterviewRepository interviewRepository;
    private final EmotionAnalysisService emotionAnalysisService;
    private final GazeAnalysisService gazeAnalysisService;
    private final SpeechAnalysisService speechAnalysisService;

    private final InterviewAnalysisRepository interviewAnalysisRepository;
    private final QuestionAnalysisRepository questionAnalysisRepository;

    @Async
    @Override
    @Transactional
    public void startAnalysisAsync(Long interviewId, List<String> videoUrls) {
        try {
            log.info("AI 분석 시작: interviewId={}, videoCount={}", interviewId, videoUrls.size());

            // 1. FastAPI 분석 수행
            AnalysisResponse analysisResponse = fastApiClientService.analyzeInterview(interviewId, videoUrls);

            // 2. 분석 결과 저장 (AnalysisDataService에 위임)
            saveAnalysisResult(interviewId, analysisResponse);

            log.info("AI 분석 완료: interviewId={}", interviewId);

        } catch (Exception e) {
            markAnalysisAsFailed(interviewId, e.getMessage());
        }
    }

    private void saveAnalysisResult(Long interviewId, AnalysisResponse analysisResponse) {

        // InterviewAnalysis 생성 또는 조회
        InterviewAnalysis interviewAnalysis = createOrGetInterviewAnalysis(interviewId);

        // 질문별 분석 결과 저장
        for (QuestionAnalysisResult questionResult : analysisResponse.getQuestions()) {
            QuestionAnalysis questionAnalysis = createQuestionAnalysis(interviewAnalysis, questionResult);

            // 각 분석 서비스에 위임
            emotionAnalysisService.save(questionAnalysis, questionResult.getEmotions());
            gazeAnalysisService.save(questionAnalysis, questionResult.getGazeData());
            speechAnalysisService.save(questionAnalysis, questionResult.getSpeechData());
        }

        Interview interview = interviewAnalysis.getInterview();
        interview.markAsAnalyzed();
    }

    private void markAnalysisAsFailed(Long interviewId, String errorMessage) {
        // TODO: 나중에 분석 상태 관리 추가 시 구현
    }

    private InterviewAnalysis createOrGetInterviewAnalysis(Long interviewId) {
        if (interviewAnalysisRepository.existsByInterviewId(interviewId)) {
            return interviewAnalysisRepository.findByInterviewId(interviewId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_NOT_FOUND));
        }

        Interview interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.INTERVIEW_NOT_FOUND));
        InterviewAnalysis interviewAnalysis = InterviewAnalysis.from(interview);

        return interviewAnalysisRepository.save(interviewAnalysis);
    }

    private QuestionAnalysis createQuestionAnalysis(InterviewAnalysis interviewAnalysis, QuestionAnalysisResult questionResult) {

        QuestionAnalysis questionAnalysis = QuestionAnalysis.of(
            interviewAnalysis,
            questionResult.getQuestionNumber().intValue()
        );

        return questionAnalysisRepository.save(questionAnalysis);
    }
}
