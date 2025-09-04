package com.judy.quizcore.quizcore.quizsession.service;

import com.judy.quizcore.quizcore.quizquestion.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizquestion.repository.QuizSessionJpaRepository;
import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizSessionService {

    private final QuizSessionJpaRepository quizSessionJpaRepository;

    public QuizSessionEntityDto startQuizSession(Long userId) {
        QuizSession quizSession = QuizSession.of(userId);
        quizSessionJpaRepository.save(quizSession);
        return new QuizSessionEntityDto(
                quizSession.getId(),
                quizSession.getSessionName(),
                quizSession.getUserId(),
                quizSession.getSessionStatus(),
                quizSession.getCorrectAnswers(),
                quizSession.getStartedDateTime(),
                quizSession.getCompletedDateTime()
        );
    }
}
