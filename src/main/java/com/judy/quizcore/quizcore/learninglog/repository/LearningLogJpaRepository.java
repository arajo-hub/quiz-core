package com.judy.quizcore.quizcore.learninglog.repository;

import com.judy.quizcore.quizcore.learninglog.entities.LearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningLogJpaRepository extends JpaRepository<LearningLog, Long> {
}
