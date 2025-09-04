package com.judy.quizcore.quizcore.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 퀴즈 답변 요청 DTO
 * 사용자가 퀴즈 문제에 답변할 때 전송하는 데이터입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerRequest {
    
    /**
     * 퀴즈 문제 ID
     */
    private Long questionId;
    
    /**
     * 사용자 답변
     * 빈칸 뚫기 문제의 경우: [{"index": 1, "answer": "quarterly"}, {"index": 3, "answer": "exceeded"}]
     */
    private List<AnswerItem> answers;
    
    /**
     * 답변 소요 시간 (초)
     */
    private Integer timeSpent;
    
    /**
     * 개별 답변 아이템
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerItem {
        private Integer index;
        private String answer;
    }
}
