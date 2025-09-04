package com.judy.quizcore.quizcore.learninglog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.learninglog.entities.LearningLog;
import com.judy.quizcore.quizcore.learninglog.repository.LearningLogJpaRepository;
import com.judy.quizcore.quizcore.quiz.dto.QuizAnswerRequest;
import com.judy.quizcore.quizcore.quiz.response.QuizAnswerResponse;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 학습 로그 서비스
 * 사용자의 학습 기록을 관리합니다.
 */
@Transactional
@Service
@RequiredArgsConstructor
public class LearningLogService {

    private final LearningLogJpaRepository learningLogJpaRepository;
    private final ObjectMapper objectMapper;

    /**
     * 학습 로그를 저장합니다.
     * 
     * @param userId 사용자 ID
     * @param quizQuestionDto 퀴즈 문제 DTO
     * @param request 답변 요청
     * @param response 채점 결과
     */
    public void saveLearningLog(Long userId, QuizQuestionEntityDto quizQuestionDto,
                               QuizAnswerRequest request, QuizAnswerResponse response) {
        try {
            String userAnswerJson = objectMapper.writeValueAsString(request.getAnswers());
            
            LearningLog learningLog = LearningLog.of(
                userId, 
                quizQuestionDto.id(), 
                response.getGradingResult().getIsCorrect(), 
                userAnswerJson, 
                request.getTimeSpent()
            );
            
            learningLogJpaRepository.save(learningLog);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }
}
