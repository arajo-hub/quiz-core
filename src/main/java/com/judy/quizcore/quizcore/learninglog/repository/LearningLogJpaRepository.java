package com.judy.quizcore.quizcore.learninglog.repository;

import com.judy.quizcore.quizcore.learninglog.entities.LearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningLogJpaRepository extends JpaRepository<LearningLog, Long> {
    
    /**
     * 퀴즈 문제 ID로 학습 로그를 조회합니다.
     * 
     * @param quizQuestionId 퀴즈 문제 ID
     * @return 학습 로그 (Optional)
     */
    @Query("SELECT l FROM LearningLog l WHERE l.quizQuestion.id = :quizQuestionId")
    Optional<LearningLog> findByQuizQuestionId(@Param("quizQuestionId") Long quizQuestionId);
}
