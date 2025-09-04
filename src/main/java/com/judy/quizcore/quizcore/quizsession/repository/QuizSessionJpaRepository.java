package com.judy.quizcore.quizcore.quizsession.repository;

import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import com.judy.quizcore.quizcore.quizsession.enums.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuizSessionJpaRepository extends JpaRepository<QuizSession, Long> {
    Optional<QuizSession> findByUserIdAndSessionStatusAndSessionType(Long userId, SessionStatus sessionStatus, SessionType sessionType);
}
