package com.capstone.godofinterview.domain.feedback.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.godofinterview.domain.analysis.entity.EmotionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.FillerWordCount;
import com.capstone.godofinterview.domain.analysis.entity.GazeAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.InterviewAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.QuestionAnalysis;
import com.capstone.godofinterview.domain.analysis.entity.SpeechAnalysis;
import com.capstone.godofinterview.domain.analysis.repository.InterviewAnalysisRepository;
import com.capstone.godofinterview.domain.feedback.dto.response.EmotionFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.dto.response.GazeFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.dto.response.InterviewFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.dto.response.OverAllFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.dto.response.QuestionFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.dto.response.SpeechFeedbackResponse;
import com.capstone.godofinterview.domain.feedback.exception.FeedbackErrorCode;
import com.capstone.godofinterview.domain.feedback.exception.FeedbackException;
import com.capstone.godofinterview.domain.feedback.util.GradeCalculator;
import com.capstone.godofinterview.domain.feedback.util.ScoreCalculator;
import com.capstone.godofinterview.domain.interview.entity.Interview;
import com.capstone.godofinterview.domain.interview.service.InterviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final InterviewService interviewService;
    private final InterviewAnalysisRepository interviewAnalysisRepository;
    private final ScoreCalculator scoreCalculator;
    private final GradeCalculator gradeCalculator;

    @Transactional(readOnly = true)
    @Override
    public InterviewFeedbackResponse getInterviewFeedback(Long interviewId, Long userId) {
        log.info("면접 피드백 조회 시작: interviewId={}, userId={}", interviewId, userId);

        // 1. 면접 정보 조회 및 권한 검증
        Interview interview = interviewService.getInterview(interviewId);
        validateUser(interview, userId);

        // 2. 분석 결과 조회
        InterviewAnalysis interviewAnalysis = interviewAnalysisRepository
            .findByInterviewIdWithAllAnalysis(interviewId)
            .orElseThrow(() -> new FeedbackException(FeedbackErrorCode.ANALYSIS_NOT_FOUND));

        // 3. 질문별 분석 결과
        List<QuestionFeedbackResponse> questionFeedbacks = createQuestionFeedbacks(interviewAnalysis);

        // 4. 종합 피드백 계산
        OverAllFeedbackResponse overallFeedback = createOverallFeedback(questionFeedbacks);

        return new InterviewFeedbackResponse(
            interview.getId(),
            interview.getJob().getName(),
            interview.getCreatedAt(),
            overallFeedback,
            questionFeedbacks
        );
    }

    /**
     * 사용자 권한 검증
     */
    private void validateUser(Interview interview, Long userId) {
        if (!interview.getUser().getId().equals(userId)) {
            throw new FeedbackException(FeedbackErrorCode.UNAUTHORIZED_FEEDBACK_ACCESS);
        }
    }

    /**
     * 질문별 피드백 생성
     */
    private List<QuestionFeedbackResponse> createQuestionFeedbacks(InterviewAnalysis interviewAnalysis) {
        List<QuestionFeedbackResponse> feedbacks = new ArrayList<>();

        for (QuestionAnalysis questionAnalysis : interviewAnalysis.getQuestionAnalyses()) {

            // 각 분석 영역별 피드백 생성
            EmotionFeedbackResponse emotionFeedback = createEmotionFeedback(questionAnalysis.getEmotionAnalysis());
            GazeFeedbackResponse gazeFeedback = createGazeFeedback(questionAnalysis.getGazeAnalysis());
            SpeechFeedbackResponse speechFeedback = createSpeechFeedback(questionAnalysis.getSpeechAnalysis());

            // 질문별 종합 점수 계산
            int questionScore = scoreCalculator.calculateQuestionScore(
                emotionFeedback.getScore(),
                gazeFeedback.getScore(),
                speechFeedback.getScore()
            );

            // 질문별 피드백 메시지 생성
            String positivePoints = generatePositivePoints(emotionFeedback, gazeFeedback, speechFeedback);
            String improvementPoints = generateImprovementPoints(emotionFeedback, gazeFeedback, speechFeedback);
            String advice = generateAdvice(emotionFeedback.getScore(), gazeFeedback.getScore(), speechFeedback.getScore());

            feedbacks.add(new QuestionFeedbackResponse(
                questionAnalysis.getQuestionNumber(),
                "질문 " + questionAnalysis.getQuestionNumber(), // TODO: 실제 질문 내용으로 변경
                questionScore,
                emotionFeedback,
                gazeFeedback,
                speechFeedback,
                positivePoints,
                improvementPoints,
                advice
            ));
        }

        return feedbacks;
    }

    /**
     * 표정 피드백 생성
     */
    private EmotionFeedbackResponse createEmotionFeedback(EmotionAnalysis emotionAnalysis) {
        if (emotionAnalysis == null) {
            return new EmotionFeedbackResponse(0, "분석 없음", 0.0, 0.0, "표정 분석 데이터가 없습니다.");
        }

        Map<String, Double> emotions = new HashMap<>();
        emotions.put("happy", emotionAnalysis.getHappy());
        emotions.put("neutral", emotionAnalysis.getNeutral());
        emotions.put("angry", emotionAnalysis.getAngry());
        emotions.put("disgust", emotionAnalysis.getDisgust());
        emotions.put("fear", emotionAnalysis.getFear());
        emotions.put("sad", emotionAnalysis.getSad());
        emotions.put("surprise", emotionAnalysis.getSurprise());

        int score = scoreCalculator.calculateEmotionScore(emotions);
        String dominantEmotion = getDominantEmotion(emotions);
        String comment = generateEmotionComment(score, dominantEmotion);

        return new EmotionFeedbackResponse(
            score,
            dominantEmotion,
            emotionAnalysis.getHappy(),
            emotionAnalysis.getNeutral(),
            comment
        );
    }

    /**
     * 시선 피드백 생성
     */
    private GazeFeedbackResponse createGazeFeedback(GazeAnalysis gazeAnalysis) {
        if (gazeAnalysis == null) {
            return new GazeFeedbackResponse(0, 0.0, "시선 분석 데이터가 없습니다.");
        }

        int score = scoreCalculator.calculateGazeScore(gazeAnalysis.getCameraAttentionRatio());
        String comment = generateGazeComment(score, gazeAnalysis.getCameraAttentionRatio());

        return new GazeFeedbackResponse(
            score,
            gazeAnalysis.getCameraAttentionRatio(),
            comment
        );
    }

    /**
     * 발음/습관어 피드백 생성
     */
    private SpeechFeedbackResponse createSpeechFeedback(SpeechAnalysis speechAnalysis) {
        if (speechAnalysis == null) {
            return new SpeechFeedbackResponse(0, 0, new HashMap<>(), "음성 분석 데이터가 없습니다.");
        }

        Map<String, Integer> fillerDetails = speechAnalysis.getFillerWordCounts().stream()
            .collect(Collectors.toMap(
                FillerWordCount::getWord,
                FillerWordCount::getCount
            ));

        int score = scoreCalculator.calculateSpeechScore(fillerDetails, 2); // 평균 2분으로 가정
        String comment = generateSpeechComment(score, speechAnalysis.getTotalFillerWords());

        return new SpeechFeedbackResponse(
            score,
            speechAnalysis.getTotalFillerWords(),
            fillerDetails,
            comment
        );
    }

    /**
     * 종합 피드백 생성
     */
    private OverAllFeedbackResponse createOverallFeedback(List<QuestionFeedbackResponse> questionFeedbacks) {

        // 영역별 평균 점수 계산
        int avgEmotionScore = (int)questionFeedbacks.stream()
            .mapToInt(q -> q.getEmotionFeedbackResponse().getScore())
            .average()
            .orElse(0);

        int avgGazeScore = (int)questionFeedbacks.stream()
            .mapToInt(q -> q.getGazeFeedbackResponse().getScore())
            .average()
            .orElse(0);

        int avgSpeechScore = (int)questionFeedbacks.stream()
            .mapToInt(q -> q.getSpeechFeedbackResponse().getScore())
            .average()
            .orElse(0);

        // 종합 점수 및 등급 계산
        int totalScore = scoreCalculator.calculateOverallScore(avgEmotionScore, avgGazeScore, avgSpeechScore);
        String grade = gradeCalculator.calculateGrade(totalScore);
        String overallComment = gradeCalculator.getGradeComment(grade);

        // 강점 및 개선점 생성
        String strengths = generateStrengths(avgEmotionScore, avgGazeScore, avgSpeechScore);
        String improvements = gradeCalculator.getImprovementSuggestion(avgEmotionScore, avgGazeScore, avgSpeechScore);

        return new OverAllFeedbackResponse(
            totalScore,
            grade,
            overallComment,
            avgEmotionScore,
            avgGazeScore,
            avgSpeechScore,
            strengths,
            improvements
        );
    }

    // ============ 헬퍼 메서드 ============

    private String getDominantEmotion(Map<String, Double> emotions) {
        return emotions.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> {
                String emotion = entry.getKey();
                return switch (emotion) {
                    case "happy" -> "긍정적";
                    case "neutral" -> "자연스러운";
                    case "sad" -> "침착한";
                    default -> "안정적인";
                };
            })
            .orElse("자연스러운");
    }

    private String generateEmotionComment(int score, String dominantEmotion) {
        if (score >= 85) return dominantEmotion + " 표정이 매우 좋았어요!";
        else if (score >= 70) return dominantEmotion + " 표정이 전반적으로 양호해요";
        else return "표정에서 긴장감이 느껴져요. 더 자연스럽게 해보세요";
    }

    private String generateGazeComment(int score, double ratio) {
        if (score >= 85) return String.format("카메라 응시율 %.1f%% - 훌륭한 아이컨택!", ratio);
        else if (score >= 70) return String.format("카메라 응시율 %.1f%% - 양호해요", ratio);
        else return "카메라를 더 자주 바라보세요";
    }

    private String generateSpeechComment(int score, int totalFillers) {
        if (score >= 85) return "습관어 사용이 적절해요!";
        else if (score >= 70) return String.format("습관어 %d회 사용 - 보통 수준이에요", totalFillers);
        else return String.format("습관어 %d회 사용 - 줄여보세요", totalFillers);
    }

    private String generatePositivePoints(EmotionFeedbackResponse emotion, GazeFeedbackResponse gaze, SpeechFeedbackResponse speech) {
        List<String> points = new ArrayList<>();

        if (emotion.getScore() >= 80) points.add("자연스러운 표정");
        if (gaze.getScore() >= 80) points.add("좋은 아이컨택");
        if (speech.getScore() >= 80) points.add("적절한 발음");

        return points.isEmpty() ? "성실한 답변 자세" : String.join(", ", points);
    }

    private String generateImprovementPoints(EmotionFeedbackResponse emotion, GazeFeedbackResponse gaze, SpeechFeedbackResponse speech) {
        List<String> points = new ArrayList<>();

        if (emotion.getScore() < 70) points.add("표정 관리");
        if (gaze.getScore() < 70) points.add("시선 처리");
        if (speech.getScore() < 70) points.add("습관어 줄이기");

        return points.isEmpty() ? "없음" : String.join(", ", points);
    }

    private String generateAdvice(int emotionScore, int gazeScore, int speechScore) {
        if (emotionScore < 70) return "긴장을 풀고 자연스러운 표정을 유지해보세요";
        if (gazeScore < 70) return "카메라를 더 자주 바라보며 면접관과 소통해보세요";
        if (speechScore < 70) return "답변 전 잠시 생각하는 시간을 가져보세요";
        return "전반적으로 좋은 면접이었어요!";
    }

    private String generateStrengths(int emotionScore, int gazeScore, int speechScore) {
        List<String> strengths = new ArrayList<>();

        if (emotionScore >= 85) strengths.add("뛰어난 표정 관리");
        else if (emotionScore >= 75) strengths.add("안정적인 표정");

        if (gazeScore >= 85) strengths.add("탁월한 아이컨택");
        else if (gazeScore >= 75) strengths.add("좋은 시선 처리");

        if (speechScore >= 85) strengths.add("명확한 발음");
        else if (speechScore >= 75) strengths.add("적절한 발음");

        return strengths.isEmpty() ? "성실한 면접 자세" : String.join(", ", strengths);
    }
}