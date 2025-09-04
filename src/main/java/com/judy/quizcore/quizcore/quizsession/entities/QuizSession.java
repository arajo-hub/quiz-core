package com.judy.quizcore.quizcore.quizsession.entities;

import com.judy.quizcore.quizcore.common.entities.BaseEntity;
import com.judy.quizcore.quizcore.quizquestion.entities.QuizQuestion;
import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 퀴즈 세션 엔티티
 * 사용자의 퀴즈 풀이 세션을 관리합니다.
 * 세션의 상태, 문제 수, 정답 수, 시작/완료 시간 등을 추적합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizSession extends BaseEntity {
    
    /**
     * 퀴즈 세션 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 퀴즈 세션 이름
     * 사용자가 세션을 구분하기 위한 이름입니다.
     * 예: "오늘의 퀴즈", "주말 복습" 등
     * 최대 200자까지 저장 가능합니다.
     */
    @Column(length = 200)
    private String sessionName;
    
    /**
     * 사용자 식별자
     * 퀴즈 세션을 생성한 사용자를 구분합니다.
     * 최대 100자까지 저장 가능합니다.
     */
    @Column(length = 100)
    private Long userId;
    
    /**
     * 세션 상태
     * 퀴즈 세션의 현재 상태를 나타냅니다.
     * ACTIVE: 진행 중, COMPLETED: 완료됨
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus sessionStatus = SessionStatus.ACTIVE;
    
    /**
     * 정답 개수
     * 사용자가 맞힌 문제의 개수입니다.
     * 기본값은 0입니다.
     */
    @Column
    private int correctAnswers = 0;
    
    /**
     * 세션 시작 시간
     * 퀴즈 세션이 시작된 시간입니다.
     */
    @Column
    private LocalDateTime startedDateTime;
    
    /**
     * 세션 완료 시간
     * 퀴즈 세션이 완료된 시간입니다.
     * 세션이 완료되기 전까지는 null입니다.
     */
    @Column
    private LocalDateTime completedDateTime;
    
    /**
     * 퀴즈 문제 목록
     * 이 세션에서 출제되는 모든 문제를 포함합니다.
     * 일대다 관계로 연결됩니다.
     */
    @OneToMany(mappedBy = "quizSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizQuestion> quizQuestions;

    public static QuizSession of(Long userId) {
        QuizSession quizSession = new QuizSession();
        quizSession.userId = userId;
        quizSession.sessionName = "오늘의 문장";
        quizSession.startedDateTime = LocalDateTime.now();
        return quizSession;
    }
}
