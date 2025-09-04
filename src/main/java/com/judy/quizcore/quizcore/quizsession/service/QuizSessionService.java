package com.judy.quizcore.quizcore.quizsession.service;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.quizsession.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.enums.SessionType;
import com.judy.quizcore.quizcore.quizsession.repository.QuizSessionJpaRepository;
import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;
import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                quizSession.getSessionType(),
                quizSession.getUserId(),
                quizSession.getSessionStatus(),
                quizSession.getCorrectAnswer(),
                quizSession.getStartedDateTime(),
                quizSession.getCompletedDateTime()
        );
    }
    
    /**
     * 복습 퀴즈 세션을 시작합니다.
     */
    public QuizSessionEntityDto startReviewSession(Long userId) {
        QuizSession quizSession = QuizSession.of(userId, SessionType.REVIEW);
        quizSessionJpaRepository.save(quizSession);
        return new QuizSessionEntityDto(
                quizSession.getId(),
                quizSession.getSessionType(),
                quizSession.getUserId(),
                quizSession.getSessionStatus(),
                quizSession.getCorrectAnswer(),
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
    
    /**
     * ID로 퀴즈 세션을 조회합니다.
     */
    public QuizSessionEntityDto findSessionById(Long sessionId) {
        QuizSession quizSession = quizSessionJpaRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_SESSION_NOT_FOUND));
        return new QuizSessionEntityDto(
                quizSession.getId(),
                quizSession.getSessionType(),
                quizSession.getUserId(),
                quizSession.getSessionStatus(),
                quizSession.getCorrectAnswer(),
                quizSession.getStartedDateTime(),
                quizSession.getCompletedDateTime()
        );
    }

    public Optional<QuizSessionEntityDto> findOngoingSessionByUserIdAndSessionType(Long userId, SessionType sessionType) {
        return quizSessionJpaRepository.findByUserIdAndSessionStatusAndSessionType(userId, SessionStatus.ACTIVE, sessionType)
                .map(QuizSessionEntityDto::from);
    }
}
