package com.judy.quizcore.quizcore.quizsession.dto;

import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import com.judy.quizcore.quizcore.quizsession.enums.SessionType;

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
}
