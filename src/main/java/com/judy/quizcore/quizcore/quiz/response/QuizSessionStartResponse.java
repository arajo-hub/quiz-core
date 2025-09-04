package com.judy.quizcore.quizcore.quiz.response;

import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import lombok.Getter;

@Getter
public class QuizSessionStartResponse {

    private Long sessionId;
    private QuizQuestionEntityDto quiz;

    public QuizSessionStartResponse(Long sessionId, QuizQuestionEntityDto quizQuestionEntityDto) {
        this.sessionId = sessionId;
        this.quiz = quizQuestionEntityDto;
    }
}
