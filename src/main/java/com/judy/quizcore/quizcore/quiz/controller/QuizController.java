package com.judy.quizcore.quizcore.quiz.controller;

import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.quiz.dto.QuizAnswerRequest;
import com.judy.quizcore.quizcore.quiz.response.QuizAnswerResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionStartResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionResultResponse;
import com.judy.quizcore.quizcore.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 퀴즈 답변 채점
     */
    @PostMapping("/quiz/grade")
    public ApiResponse<QuizAnswerResponse> grade(@RequestBody QuizAnswerRequest request) {
        return quizService.gradeQuizAnswer(1L, request);
    }
    
    /**
     * 퀴즈 세션 결과 조회
     */
    @GetMapping("/quiz/session/{sessionId}/result")
    public ApiResponse<QuizSessionResultResponse> getSessionResult(@PathVariable Long sessionId) {
        return quizService.getSessionResult(sessionId, 1L);
    }
}
