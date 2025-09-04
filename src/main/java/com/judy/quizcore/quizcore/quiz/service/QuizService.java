package com.judy.quizcore.quizcore.quiz.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.learninglog.service.LearningLogService;
import com.judy.quizcore.quizcore.quiz.dto.QuizAnswerRequest;
import com.judy.quizcore.quizcore.quiz.response.QuizAnswerResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionStartResponse;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import com.judy.quizcore.quizcore.quizquestion.service.QuizQuestionService;
import com.judy.quizcore.quizcore.quizsession.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.service.QuizSessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSessionService quizSessionService;
    private final QuizQuestionService quizQuestionService;
    private final LearningLogService learningLogService;
    private final ObjectMapper objectMapper;

    public ApiResponse<QuizSessionStartResponse> startQuizSession(Long userId) {
        // 퀴즈 세션 시작
        QuizSessionEntityDto quizSessionEntityDto = quizSessionService.startQuizSession(userId);

        // 퀴즈 문제 생성 (sessionId로 자동으로 questionOrder 계산)
        QuizQuestionEntityDto quizQuestionEntityDto = quizQuestionService.createQuizQuestion(userId, quizSessionEntityDto.id());

        return ApiResponse.success(new QuizSessionStartResponse(quizSessionEntityDto.id(), quizQuestionEntityDto));
    }

    /**
     * 퀴즈 답변을 채점하고 결과를 저장합니다.
     * 
     * @param userId 사용자 ID
     * @param request 답변 요청
     * @return 채점 결과
     */
    public ApiResponse<QuizAnswerResponse> gradeQuizAnswer(Long userId, QuizAnswerRequest request) {
        // 퀴즈 문제 조회
        QuizQuestionEntityDto quizQuestionDto = quizQuestionService.findQuizQuestionById(request.getQuestionId());
        
        // 전체 정답 여부 확인
        boolean isCorrect = checkAllAnswersCorrect(quizQuestionDto, request.getAnswers());
        
        // 채점 결과 생성
        QuizAnswerResponse response = new QuizAnswerResponse(
            quizQuestionDto.learningSentence().sentence(), 
            isCorrect
        );
        
        // LearningLog 저장
        learningLogService.saveLearningLog(userId, quizQuestionDto, request, response);
        
        return ApiResponse.success(response);
    }
    
    /**
     * 모든 답변이 정답인지 확인합니다.
     * 
     * @param quizQuestionDto 퀴즈 문제 DTO
     * @param userAnswers 사용자 답변
     * @return 모든 답변이 정답이면 true
     */
    private boolean checkAllAnswersCorrect(QuizQuestionEntityDto quizQuestionDto, List<QuizAnswerRequest.AnswerItem> userAnswers) {
        try {
            // correctAnswer가 이미 Object로 파싱되어 있으므로 직접 캐스팅
            Object correctAnswerObj = quizQuestionDto.correctAnswer();
            if (!(correctAnswerObj instanceof List)) {
                throw new RuntimeException("정답 형식이 올바르지 않습니다.");
            }
            
            List<Object> correctAnswers = (List<Object>) correctAnswerObj;
            
            // 사용자 답변과 정답 비교
            for (QuizAnswerRequest.AnswerItem userAnswer : userAnswers) {
                boolean found = false;
                
                // 해당 인덱스의 정답 찾기
                for (Object answerObj : correctAnswers) {
                    if (answerObj instanceof java.util.Map) {
                        java.util.Map<String, Object> answerMap = (java.util.Map<String, Object>) answerObj;
                        Integer answerIndex = (Integer) answerMap.get("index");
                        if (answerIndex != null && answerIndex.equals(userAnswer.getIndex())) {
                            String correctAnswer = (String) answerMap.get("answer");
                            if (!correctAnswer.equals(userAnswer.getAnswer())) {
                                return false; // 하나라도 틀리면 false
                            }
                            found = true;
                            break;
                        }
                    }
                }
                
                if (!found) {
                    return false; // 해당 인덱스의 정답을 찾을 수 없으면 false
                }
            }
            
            return true; // 모든 답변이 정답
        } catch (Exception e) {
            throw new RuntimeException("정답 처리 중 오류가 발생했습니다.", e);
        }
    }
}
