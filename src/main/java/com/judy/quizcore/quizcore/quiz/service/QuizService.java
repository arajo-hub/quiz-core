package com.judy.quizcore.quizcore.quiz.service;

import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionStartResponse;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import com.judy.quizcore.quizcore.quizquestion.service.QuizQuestionService;
import com.judy.quizcore.quizcore.quizsession.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.service.QuizSessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSessionService quizSessionService;
    private final QuizQuestionService quizQuestionService;

    public ApiResponse<QuizSessionStartResponse> startQuizSession(Long userId) {
        // 퀴즈 세션 시작
        QuizSessionEntityDto quizSessionEntityDto = quizSessionService.startQuizSession(userId);

        // 퀴즈 문제 생성 (sessionId로 자동으로 questionOrder 계산)
        QuizQuestionEntityDto quizQuestionEntityDto = quizQuestionService.createQuizQuestion(userId, quizSessionEntityDto.id());

        return ApiResponse.success(new QuizSessionStartResponse(quizSessionEntityDto.id(), quizQuestionEntityDto));
    }


}
