package com.judy.quizcore.quizcore.quiz.response;

import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 퀴즈 답변 채점 결과 응답 DTO
 * 사용자의 답변에 대한 채점 결과를 반환합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerResponse {
    
    /**
     * 채점 결과
     */
    private GradingResult gradingResult;
    
    /**
     * 다음 문제
     */
    private QuizQuestionEntityDto nextQuestion;
    
    /**
     * 퀴즈 세션 완료 여부
     */
    private Boolean isSessionCompleted;
    
    /**
     * 채점 결과
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradingResult {
        private String sentence;
        private Boolean isCorrect;
    }
}
