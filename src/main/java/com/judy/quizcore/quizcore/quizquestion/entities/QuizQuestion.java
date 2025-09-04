package com.judy.quizcore.quizcore.quizquestion.entities;

import com.judy.quizcore.quizcore.common.entities.BaseEntity;
import com.judy.quizcore.quizcore.learninglog.entities.LearningLog;
import com.judy.quizcore.quizcore.learningsentence.entities.LearningSentence;
import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.judy.quizcore.quizcore.quizquestion.enums.QuestionType;

/**
 * 퀴즈 문제 엔티티
 * 퀴즈 세션에서 출제되는 개별 문제를 관리합니다.
 * 문제의 내용, 정답, 보기, 순서 등을 저장합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizQuestion extends BaseEntity {
    
    /**
     * 퀴즈 문제 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 퀴즈 세션
     * 이 문제가 속한 퀴즈 세션입니다.
     * 다대일 관계로 연결됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private QuizSession quizSession;
    
    /**
     * 학습 문장
     * 이 문제의 기반이 되는 학습 문장입니다.
     * 다대일 관계로 연결됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private LearningSentence learningSentence;
    
    /**
     * 문제 순서
     * 퀴즈 세션 내에서 문제가 출제되는 순서입니다.
     * 1부터 시작하는 순차적인 번호입니다.
     */
    @Column(nullable = false)
    private Integer questionOrder;
    
    /**
     * 문제 유형
     * 문제의 출제 형태를 나타냅니다.
     * TRANSLATION(번역), COMPLETION(빈칸채우기), MULTIPLE_CHOICE(객관식) 등
     * enum으로 정의되어 타입 안정성을 보장합니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;
    
    /**
     * 문제 내용
     * 사용자에게 보여지는 실제 문제 텍스트입니다.
     * 최대 1000자까지 저장 가능합니다.
     */
    @Column(length = 1000)
    private String questionText;
    
    /**
     * 정답
     * 문제의 정답을 저장합니다.
     * 최대 1000자까지 저장 가능합니다.
     */
    @Column(length = 1000)
    private String correctAnswer;
    
    /**
     * 문제 보기
     * 객관식 문제의 경우 선택지를 JSON 형태로 저장합니다.
     * 예: ["선택지1", "선택지2", "선택지3", "선택지4"]
     * TEXT 타입으로 저장되어 JSON 데이터를 유연하게 처리할 수 있습니다.
     */
    @Column(columnDefinition = "TEXT")
    private String options;
    
    /**
     * 학습 로그
     * 이 문제에 대한 사용자의 답변 기록입니다.
     * 일대일 관계로 연결됩니다.
     */
    @OneToOne(mappedBy = "quizQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LearningLog learningLog;
}
