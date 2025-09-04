package com.judy.quizcore.quizcore.learninglog.entities;

import com.judy.quizcore.quizcore.common.entities.BaseEntity;
import com.judy.quizcore.quizcore.quizquestion.entities.QuizQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 학습 로그 엔티티
 * 사용자가 퀴즈 문제를 풀 때마다 생성되는 결과 기록입니다.
 * 정답 여부, 사용자 답변, 소요 시간 등을 추적하여 학습 효과를 분석할 수 있습니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LearningLog extends BaseEntity {
    
    /**
     * 학습 로그 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 식별자
     * 퀴즈 로그를 생성한 사용자를 구분합니다.
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 퀴즈 문제
     * 이 로그가 기록된 퀴즈 문제입니다.
     * 다대일 관계로 연결됩니다.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private QuizQuestion quizQuestion;
    
    /**
     * 정답 여부
     * 사용자가 문제를 맞혔는지 여부를 나타냅니다.
     * true: 정답, false: 오답
     */
    @Column(nullable = false)
    private Boolean isCorrect;
    
    /**
     * 사용자 답변
     * 사용자가 입력한 답변을 JSON 형태로 저장합니다.
     * TEXT 타입으로 저장되어 JSON 데이터를 유연하게 처리할 수 있습니다.
     */
    @Column(columnDefinition = "TEXT")
    private String userAnswer;
    
    /**
     * 답변 소요 시간 (초)
     * 사용자가 문제를 푸는데 걸린 시간을 기록합니다.
     */
    @Column
    private Integer timeSpent;
    
    /**
     * LearningLog를 생성하는 팩토리 메서드입니다.
     * 
     * @param userId 사용자 ID
     * @param quizQuestionId 퀴즈 문제 ID
     * @param isCorrect 정답 여부
     * @param userAnswer 사용자 답변 (JSON 문자열)
     * @param timeSpent 소요 시간
     * @return 생성된 LearningLog
     */
    public static LearningLog of(Long userId, Long quizQuestionId, Boolean isCorrect, 
                                String userAnswer, Integer timeSpent) {
        LearningLog learningLog = new LearningLog();
        learningLog.userId = userId;
        learningLog.quizQuestion = new QuizQuestion(quizQuestionId);
        learningLog.isCorrect = isCorrect;
        learningLog.userAnswer = userAnswer;
        learningLog.timeSpent = timeSpent;
        return learningLog;
    }
}
