package com.judy.quizcore.quizcore.quiz.service;

import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.service.QuizSessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSessionService quizSessionService;

    public ApiResponse<QuizSessionEntityDto> startQuizSession(Long userId) {
        // 퀴즈 세션 시작
        QuizSessionEntityDto quizSessionEntityDto = quizSessionService.startQuizSession(userId);
        return ApiResponse.success(quizSessionEntityDto);
    }


}
