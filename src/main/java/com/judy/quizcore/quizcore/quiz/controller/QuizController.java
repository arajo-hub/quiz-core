package com.judy.quizcore.quizcore.quiz.controller;

import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionStartResponse;
import com.judy.quizcore.quizcore.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    /**
     * 퀴즈 시작
     */
    @PostMapping("/quiz/start")
    public ApiResponse<QuizSessionStartResponse> start() {
        return quizService.startQuizSession(1L);
    }
}
