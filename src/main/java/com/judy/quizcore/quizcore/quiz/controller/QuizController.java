package com.judy.quizcore.quizcore.quiz.controller;

import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.quiz.service.QuizService;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizSessionEntityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    /**
     * 퀴즈 시작
     */
    @GetMapping("/quiz/start")
    public ApiResponse<QuizSessionEntityDto> start() {
        return quizService.startQuizSession(1L);
    }
}
