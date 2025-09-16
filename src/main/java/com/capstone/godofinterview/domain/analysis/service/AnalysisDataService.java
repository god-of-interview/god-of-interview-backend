package com.capstone.godofinterview.domain.analysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.analysis.dto.response.AnalysisResponse;
import com.capstone.godofinterview.domain.analysis.dto.response.QuestionAnalysisResult;
import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisErrorCode;
import com.capstone.godofinterview.domain.analysis.exception.AnalysisException;
import com.capstone.godofinterview.domain.analysis.repository.InterviewAnalysisRepository;
import com.capstone.godofinterview.domain.analysis.repository.QuestionAnalysisRepository;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.service.InterviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisDataService {

    private final InterviewService interviewService;
    private final InterviewAnalysisRepository interviewAnalysisRepository;
    private final QuestionAnalysisRepository questionAnalysisRepository;
    private final EmotionAnalysisService emotionAnalysisService;
    private final GazeAnalysisService gazeAnalysisService;
    private final SpeechAnalysisService speechAnalysisService;

    @Transactional
    public void saveAnalysisResult(Long interviewId, AnalysisResponse analysisResponse) {

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

    @Transactional
    public void markAnalysisAsFailed(Long interviewId, String errorMessage) {
        // TODO: 나중에 분석 상태 관리 추가 시 구현
    }

    private InterviewAnalysis createOrGetInterviewAnalysis(Long interviewId) {
        if (interviewAnalysisRepository.existsByInterviewId(interviewId)) {
            return interviewAnalysisRepository.findByInterviewId(interviewId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_NOT_FOUND));
        }

        Interview interview = interviewService.getInterview(interviewId);
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
