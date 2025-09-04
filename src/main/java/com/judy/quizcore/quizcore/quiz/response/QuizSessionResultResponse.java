package com.judy.quizcore.quizcore.quiz.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 퀴즈 세션 결과 응답 DTO
 * 퀴즈 세션 완료 후 결과 요약 정보를 제공합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSessionResultResponse {
    
    /**
     * 세션 ID
     */
    private Long sessionId;
    
    /**
     * 세션 이름
     */
    private String sessionName;
    
    /**
     * 총 문제 수
     */
    private Integer totalQuestions;
    
    /**
     * 맞힌 문제 수
     */
    private Integer correctAnswers;
    
    /**
     * 틀린 문제 수
     */
    private Integer wrongAnswers;
    
    /**
     * 정확도 (0.0 ~ 1.0)
     */
    private Double accuracy;
    
    /**
     * 룰렛 사용 여부
     */
    private Boolean hasUsedRoulette;
    
    /**
     * 시작 시간
     */
    private LocalDateTime startedDateTime;
    
    /**
     * 완료 시간
     */
    private LocalDateTime completedDateTime;
}
