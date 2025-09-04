package com.judy.quizcore.quizcore.quizquestion.repository;

import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSessionJpaRepository extends JpaRepository<QuizSession, Long> {
}
