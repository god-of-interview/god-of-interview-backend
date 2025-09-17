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
        log.info("=== 면접 피드백 조회 시작: interviewId={}, userId={} ===", interviewId, userId);

        try {
            // 1. 면접 정보 조회 및 권한 검증
            log.debug("Step 1: 면접 정보 조회 중...");
            Interview interview = interviewService.getInterview(interviewId);
            log.debug("Step 1 완료: interview.id={}, job.name={}, user.id={}",
                interview.getId(), interview.getJob().getName(), interview.getUser().getId());

            log.debug("Step 2: 권한 검증 중...");
            validateUserPermission(interview, userId);
            log.debug("Step 2 완료: 권한 검증 성공");

            // 2. 분석 결과 조회
            log.debug("Step 3: 분석 결과 조회 중...");
            InterviewAnalysis interviewAnalysis = interviewAnalysisRepository
                .findByInterviewIdWithAllAnalysis(interviewId)
                .orElseThrow(() -> {
                    log.error("분석 결과를 찾을 수 없음: interviewId={}", interviewId);
                    return new FeedbackException(FeedbackErrorCode.ANALYSIS_NOT_FOUND);
                });

            log.debug("Step 3 완료: interviewAnalysis.id={}, questionAnalyses.size={}",
                interviewAnalysis.getId(), interviewAnalysis.getQuestionAnalyses().size());

            // 3. 질문별 분석 결과 처리
            log.debug("Step 4: 질문별 피드백 생성 중...");
            List<QuestionFeedbackResponse> questionFeedbacks = createQuestionFeedbacks(interviewAnalysis);
            log.debug("Step 4 완료: questionFeedbacks.size={}", questionFeedbacks.size());

            // 4. 종합 피드백 계산
            log.debug("Step 5: 종합 피드백 계산 중...");
            OverAllFeedbackResponse overallFeedback = createOverallFeedback(questionFeedbacks);
            log.debug("Step 5 완료: totalScore={}, grade={}", overallFeedback.getTotalScore(), overallFeedback.getGrade());

            // 5. 최종 응답 생성
            log.debug("Step 6: 최종 응답 생성 중...");
            InterviewFeedbackResponse response = new InterviewFeedbackResponse(
                interview.getId(),
                interview.getJob().getName(),
                interview.getCreatedAt(),
                overallFeedback,
                questionFeedbacks
            );
            log.debug("Step 6 완료");

            log.info("=== 면접 피드백 조회 성공: interviewId={} ===", interviewId);
            return response;

        } catch (Exception e) {
            log.error("=== 면접 피드백 조회 실패: interviewId={} ===", interviewId, e);
            throw e;
        }
    }

    /**
     * 사용자 권한 검증
     */
    private void validateUserPermission(Interview interview, Long userId) {
        if (!interview.getUser().getId().equals(userId)) {
            log.error("권한 없음: interview.userId={}, requestUserId={}", interview.getUser().getId(), userId);
            throw new FeedbackException(FeedbackErrorCode.UNAUTHORIZED_FEEDBACK_ACCESS);
        }
    }

    /**
     * 질문별 피드백 생성 - 양방향 매핑으로 간단하게!
     */
    private List<QuestionFeedbackResponse> createQuestionFeedbacks(InterviewAnalysis interviewAnalysis) {
        log.debug("질문별 피드백 생성 시작");
        List<QuestionFeedbackResponse> feedbacks = new ArrayList<>();

        // 양방향 관계로 바로 접근 가능!
        for (QuestionAnalysis questionAnalysis : interviewAnalysis.getQuestionAnalyses()) {
            try {
                log.debug("질문 {} 피드백 생성 중... (id={})",
                    questionAnalysis.getQuestionNumber(), questionAnalysis.getId());

                // 각 분석 영역별 피드백 생성 (양방향 매핑으로 바로 접근)
                log.debug("- 표정 분석 피드백 생성 중...");
                EmotionFeedbackResponse emotionFeedback = createEmotionFeedback(questionAnalysis.getEmotionAnalysis());
                log.debug("- 표정 분석 완료: score={}", emotionFeedback.getScore());

                log.debug("- 시선 분석 피드백 생성 중...");
                GazeFeedbackResponse gazeFeedback = createGazeFeedback(questionAnalysis.getGazeAnalysis());
                log.debug("- 시선 분석 완료: score={}", gazeFeedback.getScore());

                log.debug("- 음성 분석 피드백 생성 중...");
                SpeechFeedbackResponse speechFeedback = createSpeechFeedback(questionAnalysis.getSpeechAnalysis());
                log.debug("- 음성 분석 완료: score={}", speechFeedback.getScore());

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

                log.debug("질문 {} 피드백 생성 완료", questionAnalysis.getQuestionNumber());

            } catch (Exception e) {
                log.error("질문 {} 피드백 생성 실패", questionAnalysis.getQuestionNumber(), e);
                throw e;
            }
        }

        log.debug("질문별 피드백 생성 완료: 총 {}개", feedbacks.size());
        return feedbacks;
    }

    /**
     * 표정 피드백 생성
     */
    private EmotionFeedbackResponse createEmotionFeedback(EmotionAnalysis emotionAnalysis) {
        try {
            if (emotionAnalysis == null) {
                log.debug("EmotionAnalysis가 null임");
                return new EmotionFeedbackResponse(0, "분석 없음", 0.0, 0.0, "표정 분석 데이터가 없습니다.");
            }

            log.debug("EmotionAnalysis 처리: happy={}, neutral={}, angry={}",
                emotionAnalysis.getHappy(), emotionAnalysis.getNeutral(), emotionAnalysis.getAngry());

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

        } catch (Exception e) {
            log.error("표정 피드백 생성 중 예외 발생", e);
            return new EmotionFeedbackResponse(0, "오류", 0.0, 0.0, "표정 분석 처리 중 오류 발생");
        }
    }

    /**
     * 시선 피드백 생성
     */
    private GazeFeedbackResponse createGazeFeedback(GazeAnalysis gazeAnalysis) {
        try {
            if (gazeAnalysis == null) {
                log.debug("GazeAnalysis가 null임");
                return new GazeFeedbackResponse(0, 0.0, "시선 분석 데이터가 없습니다.");
            }

            log.debug("GazeAnalysis 처리: cameraAttentionRatio={}",
                gazeAnalysis.getCameraAttentionRatio());

            int score = scoreCalculator.calculateGazeScore(gazeAnalysis.getCameraAttentionRatio());
            String comment = generateGazeComment(score, gazeAnalysis.getCameraAttentionRatio());

            return new GazeFeedbackResponse(
                score,
                gazeAnalysis.getCameraAttentionRatio(),
                comment
            );

        } catch (Exception e) {
            log.error("시선 피드백 생성 중 예외 발생", e);
            return new GazeFeedbackResponse(0, 0.0, "시선 분석 처리 중 오류 발생");
        }
    }

    /**
     * 발음/습관어 피드백 생성
     */
    private SpeechFeedbackResponse createSpeechFeedback(SpeechAnalysis speechAnalysis) {
        try {
            if (speechAnalysis == null) {
                log.debug("SpeechAnalysis가 null임");
                return new SpeechFeedbackResponse(0, 0, new HashMap<>(), "음성 분석 데이터가 없습니다.");
            }

            log.debug("SpeechAnalysis 처리: totalFillerWords={}, fillerWordCounts.size={}",
                speechAnalysis.getTotalFillerWords(),
                speechAnalysis.getFillerWordCounts() != null ? speechAnalysis.getFillerWordCounts().size() : "null");

            // 양방향 관계로 바로 접근!
            Map<String, Integer> fillerDetails = new HashMap<>();

            if (speechAnalysis.getFillerWordCounts() != null && !speechAnalysis.getFillerWordCounts().isEmpty()) {
                fillerDetails = speechAnalysis.getFillerWordCounts().stream()
                    .collect(Collectors.toMap(
                        FillerWordCount::getWord,
                        FillerWordCount::getCount
                    ));
                log.debug("습관어 상세: {}", fillerDetails);
            } else {
                log.debug("습관어 데이터 없음 (빈 리스트 또는 null)");
            }

            int score = scoreCalculator.calculateSpeechScore(fillerDetails, 2); // 평균 2분으로 가정
            String comment = generateSpeechComment(score, speechAnalysis.getTotalFillerWords());

            return new SpeechFeedbackResponse(
                score,
                speechAnalysis.getTotalFillerWords(),
                fillerDetails,
                comment
            );

        } catch (Exception e) {
            log.error("음성 피드백 생성 중 예외 발생", e);
            return new SpeechFeedbackResponse(0, 0, new HashMap<>(), "음성 분석 처리 중 오류 발생");
        }
    }

    /**
     * 종합 피드백 생성
     */
    private OverAllFeedbackResponse createOverallFeedback(List<QuestionFeedbackResponse> questionFeedbacks) {
        try {
            log.debug("종합 피드백 계산 시작: questionFeedbacks.size={}", questionFeedbacks.size());

            // 영역별 평균 점수 계산
            int avgEmotionScore = (int) questionFeedbacks.stream()
                .mapToInt(q -> q.getEmotionFeedbackResponse().getScore())
                .average()
                .orElse(0);

            int avgGazeScore = (int) questionFeedbacks.stream()
                .mapToInt(q -> q.getGazeFeedbackResponse().getScore())
                .average()
                .orElse(0);

            int avgSpeechScore = (int) questionFeedbacks.stream()
                .mapToInt(q -> q.getSpeechFeedbackResponse().getScore())
                .average()
                .orElse(0);

            log.debug("평균 점수: emotion={}, gaze={}, speech={}", avgEmotionScore, avgGazeScore, avgSpeechScore);

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

        } catch (Exception e) {
            log.error("종합 피드백 생성 중 예외 발생", e);
            throw e;
        }
    }

    // ============ 헬퍼 메서드들 ============

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