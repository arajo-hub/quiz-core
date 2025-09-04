package com.judy.quizcore.quizcore.quiz.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 퀴즈 답변 채점 결과 응답 DTO
 * 사용자의 답변에 대한 채점 결과를 반환합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerResponse {
    
    /**
     * 전체 문장
     */
    private String sentence;
    
    /**
     * 정답 여부
     */
    private Boolean isCorrect;
}
