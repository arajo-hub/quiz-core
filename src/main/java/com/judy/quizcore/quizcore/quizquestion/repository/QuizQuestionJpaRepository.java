package com.judy.quizcore.quizcore.quizquestion.repository;

import com.judy.quizcore.quizcore.quizquestion.entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionJpaRepository extends JpaRepository<QuizQuestion, Long> {
    
    /**
     * 특정 세션의 가장 큰 문제 순서를 조회합니다.
     * 
     * @param sessionId 퀴즈 세션 ID
     * @return 가장 큰 문제 순서, 없으면 null
     */
    @Query("SELECT MAX(q.questionOrder) FROM QuizQuestion q WHERE q.quizSession.id = :sessionId")
    Integer findMaxQuestionOrderBySessionId(@Param("sessionId") Long sessionId);
    
    /**
     * 특정 세션의 문제 수를 카운트합니다.
     * 
     * @param sessionId 퀴즈 세션 ID
     * @return 문제 수
     */
    @Query("SELECT COUNT(q) FROM QuizQuestion q WHERE q.quizSession.id = :sessionId")
    Integer countByQuizSessionId(@Param("sessionId") Long sessionId);
    
    /**
     * 특정 세션의 모든 문제를 문제 순서대로 조회합니다.
     * 
     * @param sessionId 퀴즈 세션 ID
     * @return 문제 순서대로 정렬된 문제 목록
     */
    @Query("SELECT q FROM QuizQuestion q WHERE q.quizSession.id = :sessionId ORDER BY q.questionOrder")
    List<QuizQuestion> findByQuizSessionIdOrderByQuestionOrder(@Param("sessionId") Long sessionId);
}
