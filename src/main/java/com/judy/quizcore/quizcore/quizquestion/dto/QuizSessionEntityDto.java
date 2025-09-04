package com.judy.quizcore.quizcore.quizquestion.dto;

import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;

import java.time.LocalDateTime;

public record QuizSessionEntityDto(
        Long id,
        String sessionName,
        Long userId,
        SessionStatus sessionStatus,
        int correctAnswer,
        LocalDateTime startedDateTime,
        LocalDateTime completedDateTime
) {
}
