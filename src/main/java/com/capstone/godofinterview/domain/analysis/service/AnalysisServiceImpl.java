package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;
import java.util.stream.Collectors;

import com.capstone.godofinterview.domain.analysis.dto.response.AnalysisResponse;
import com.capstone.godofinterview.domain.analysis.dto.response.QuestionAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.EmotionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.FillerWordCount;
import com.capstone.godofinterview.domain.analysis.entity.GazeAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.SpeechAnalysis;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisErrorCode;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisException;
import com.capstone.godofinterview.domain.analysis.repository.InterviewAnalysisRepository;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.repository.InterviewRepository;

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
    private final InterviewAnalysisRepository interviewAnalysisRepository;

    @Async
    @Override
    @Transactional
    public void startAnalysisAsync(Long interviewId, List<String> videoUrls) {
        try {
            log.info("AI 분석 시작: interviewId={}, videoCount={}", interviewId, videoUrls.size());

            // 1. FastAPI 분석 수행
            AnalysisResponse analysisResponse = fastApiClientService.analyzeInterview(interviewId, videoUrls);

            // 2. 분석 결과 저장 (통합된 방식)
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
            saveQuestionAnalysis(interviewAnalysis, questionResult);
        }

        // 모든 질문 처리 완료 후 한 번만 저장
        interviewAnalysisRepository.save(interviewAnalysis);

        // 면접 상태 업데이트
        Interview interview = interviewAnalysis.getInterview();
        interview.markAsAnalyzed();

        log.info("분석 결과 저장 완료: interviewId={}", interviewId);
    }

    private void saveQuestionAnalysis(InterviewAnalysis interviewAnalysis, QuestionAnalysisResult questionResult) {

        // 1. QuestionAnalysis 생성
        QuestionAnalysis questionAnalysis = QuestionAnalysis.of(
            interviewAnalysis,
            questionResult.getQuestionNumber().intValue()
        );

        // 표정 분석
        EmotionAnalysis emotionAnalysis = EmotionAnalysis.of(
            questionAnalysis,
            questionResult.getEmotions().getAngry(),
            questionResult.getEmotions().getDisgust(),
            questionResult.getEmotions().getFear(),
            questionResult.getEmotions().getHappy(),
            questionResult.getEmotions().getSad(),
            questionResult.getEmotions().getSurprise(),
            questionResult.getEmotions().getNeutral()
        );
        questionAnalysis.addEmotionAnalysis(emotionAnalysis);

        // 시선 분석
        GazeAnalysis gazeAnalysis = GazeAnalysis.of(
            questionAnalysis,
            questionResult.getGazeData().getCameraAttentionRatio()
        );
        questionAnalysis.addGazeAnalysis(gazeAnalysis);

        // 음성 분석
        int totalFillerWords = questionResult.getSpeechData().getFillerWordDetails().values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();

        SpeechAnalysis speechAnalysis = SpeechAnalysis.of(questionAnalysis, totalFillerWords);
        questionAnalysis.addSpeechAnalysis(speechAnalysis);

        // 습관어 상세 데이터
        List<FillerWordCount> fillerWordCounts = questionResult.getSpeechData().getFillerWordDetails()
            .entrySet()
            .stream()
            .map(entry -> FillerWordCount.of(speechAnalysis, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

        // 양방향 관계 설정
        speechAnalysis.addFillerWordCounts(fillerWordCounts);

        // 3. InterviewAnalysis에 추가 (Cascade로 모든 하위 엔티티 자동 저장)
        interviewAnalysis.getQuestionAnalyses().add(questionAnalysis);
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

    private void markAnalysisAsFailed(Long interviewId, String errorMessage) {
        log.error("분석 실패: interviewId={}, error={}", interviewId, errorMessage);
        // TODO: 분석 실패 상태 관리
    }
}
