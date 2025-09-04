package com.judy.quizcore.quizcore.quizsession.dto;

import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import com.judy.quizcore.quizcore.quizsession.enums.SessionType;
import com.judy.quizcore.quizcore.quizsession.entities.QuizSession;

import java.time.LocalDateTime;

public record QuizSessionEntityDto(
        Long id,
        SessionType sessionType,
        Long userId,
        SessionStatus sessionStatus,
        int correctAnswer,
        LocalDateTime startedDateTime,
        LocalDateTime completedDateTime
) {

    public static QuizSessionEntityDto from(QuizSession quizSession) {
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
}
