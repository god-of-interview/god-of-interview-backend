package com.capstone.godofinterview.domain.feedback.util;

import org.springframework.stereotype.Component;

@Component
public class GradeCalculator {

    /**
     * 점수를 등급으로 변환
     */
    public String calculateGrade(int score) {
        if (score >= 95) return "S";
        else if (score >= 90) return "A+";
        else if (score >= 85) return "A";
        else if (score >= 80) return "B+";
        else if (score >= 75) return "B";
        else if (score >= 70) return "C+";
        else if (score >= 65) return "C";
        else return "D";
    }

    /**
     * 등급별 코멘트 생성
     */
    public String getGradeComment(String grade) {
        return switch (grade) {
            case "S" -> "완벽한 면접이었습니다! 🏆";
            case "A+" -> "탁월한 면접 실력을 보여주셨네요! ⭐";
            case "A" -> "전반적으로 우수한 면접이었습니다! 👍";
            case "B+" -> "양호한 면접이었어요. 조금만 더 연습하면 완벽해질 것 같아요! 💪";
            case "B" -> "보통 수준의 면접이었습니다. 개선할 점들을 연습해보세요. 📚";
            case "C+" -> "면접 실력 향상이 필요해요. 꾸준히 연습해보세요! 🔥";
            case "C" -> "기본기부터 차근차근 연습이 필요합니다. 💭";
            case "D" -> "면접 준비를 더 체계적으로 해보세요. 분명 향상될 거예요! 🌟";
            default -> "면접을 완료해주셔서 감사합니다!";
        };
    }

    /**
     * 점수별 개선 제안
     */
    public String getImprovementSuggestion(int emotionScore, int gazeScore, int speechScore) {
        StringBuilder suggestion = new StringBuilder();

        if (emotionScore < 80) {
            suggestion.append("표정 관리 연습, ");
        }
        if (gazeScore < 80) {
            suggestion.append("카메라 응시 연습, ");
        }
        if (speechScore < 80) {
            suggestion.append("습관어 줄이기 연습, ");
        }

        if (suggestion.length() > 0) {
            suggestion.setLength(suggestion.length() - 2); // 마지막 ", " 제거
            return suggestion.toString();
        } else {
            return "전 영역에서 우수한 실력을 보여주셨어요!";
        }
    }
}