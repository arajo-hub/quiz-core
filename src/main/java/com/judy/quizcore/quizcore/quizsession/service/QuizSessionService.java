package com.judy.quizcore.quizcore.quizsession.service;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.quizsession.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.repository.QuizSessionJpaRepository;
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
    
    /**
     * 퀴즈 세션을 완료 상태로 변경합니다.
     */
    public void completeQuizSession(Long sessionId) {
        QuizSession quizSession = quizSessionJpaRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_SESSION_NOT_FOUND));
        quizSession.complete();
        quizSessionJpaRepository.save(quizSession);
    }
}
