package com.capstone.godofinterview.domain.feedback.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {

    /**
     * 표정 분석 점수 계산 (0-100점)
     */
    public int calculateEmotionScore(Map<String, Double> emotions) {
        // 긍정적 감정 비율
        double positive = emotions.getOrDefault("happy", 0.0) +
            emotions.getOrDefault("surprise", 0.0) * 0.5; // surprise는 50%만 긍정으로 계산

        // 중립적 감정 비율
        double neutral = emotions.getOrDefault("neutral", 0.0);

        // 부정적 감정 비율
        double negative = emotions.getOrDefault("angry", 0.0) +
            emotions.getOrDefault("disgust", 0.0) +
            emotions.getOrDefault("fear", 0.0) +
            emotions.getOrDefault("sad", 0.0);

        // 점수 계산: 긍정 100%, 중립 80%, 부정 0% 가중치
        double score = (positive * 1.0) + (neutral * 0.8) + (negative * 0.0);

        // 0-100 범위로 정규화
        return Math.min(100, Math.max(0, (int) score));
    }

    /**
     * 시선 분석 점수 계산 (0-100점)
     */
    public int calculateGazeScore(double cameraAttentionRatio) {
        // 카메라 응시율을 그대로 점수로 사용 (0-100%)
        return Math.min(100, Math.max(0, (int) cameraAttentionRatio));
    }

    /**
     * 발음/습관어 점수 계산 (0-100점)
     */
    public int calculateSpeechScore(Map<String, Integer> fillerWords, int estimatedDurationMinutes) {
        // 전체 습관어 개수
        int totalFillers = fillerWords.values().stream().mapToInt(Integer::intValue).sum();

        // 분당 습관어 개수 (면접 시간을 모르므로 평균 2분으로 가정)
        double fillersPerMinute = totalFillers / (double) Math.max(1, estimatedDurationMinutes);

        // 점수 계산: 분당 0개=100점, 1개=90점, 2개=80점, 3개=70점, 4개 이상=60점
        int score;
        if (fillersPerMinute <= 0.5) score = 100;
        else if (fillersPerMinute <= 1.0) score = 90;
        else if (fillersPerMinute <= 2.0) score = 80;
        else if (fillersPerMinute <= 3.0) score = 70;
        else if (fillersPerMinute <= 4.0) score = 60;
        else score = 50;

        return score;
    }

    /**
     * 질문별 종합 점수 계산
     */
    public int calculateQuestionScore(int emotionScore, int gazeScore, int speechScore) {
        // 가중 평균: 표정 30%, 시선 40%, 발음 30%
        return (int) (emotionScore * 0.3 + gazeScore * 0.4 + speechScore * 0.3);
    }

    /**
     * 전체 면접 종합 점수 계산
     */
    public int calculateOverallScore(int totalEmotionScore, int totalGazeScore, int totalSpeechScore) {
        // 전체 평균
        return (totalEmotionScore + totalGazeScore + totalSpeechScore) / 3;
    }
}
