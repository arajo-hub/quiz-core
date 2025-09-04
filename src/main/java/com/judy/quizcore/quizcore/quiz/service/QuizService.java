package com.judy.quizcore.quizcore.quiz.service;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.common.response.ApiResponse;
import com.judy.quizcore.quizcore.learninglog.service.LearningLogService;
import com.judy.quizcore.quizcore.quiz.dto.QuizAnswerRequest;
import com.judy.quizcore.quizcore.quiz.response.QuizAnswerResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionStartResponse;
import com.judy.quizcore.quizcore.quiz.response.QuizSessionResultResponse;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import com.judy.quizcore.quizcore.quizquestion.service.QuizQuestionService;
import com.judy.quizcore.quizcore.quizsession.dto.QuizSessionEntityDto;
import com.judy.quizcore.quizcore.quizsession.enums.SessionType;
import com.judy.quizcore.quizcore.quizsession.service.QuizSessionService;
import com.judy.quizcore.quizcore.quizsession.enums.SessionStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSessionService quizSessionService;
    private final QuizQuestionService quizQuestionService;
    private final LearningLogService learningLogService;

    public ApiResponse<QuizSessionStartResponse> startQuizSession(Long userId) {
        // 퀴즈 세션 시작
        QuizSessionEntityDto quizSessionEntityDto = quizSessionService.startQuizSession(userId);

        // 퀴즈 문제 생성 (sessionId로 자동으로 questionOrder 계산)
        QuizQuestionEntityDto quizQuestionEntityDto = quizQuestionService.createQuizQuestion(userId, quizSessionEntityDto.id());

        return ApiResponse.success(new QuizSessionStartResponse(quizSessionEntityDto.id(), quizQuestionEntityDto));
    }
    
    /**
     * 복습 퀴즈 세션을 시작합니다.
     */
    public ApiResponse<QuizSessionStartResponse> startReviewSession(Long userId) {
        // 복습 퀴즈 세션 시작
        QuizSessionEntityDto quizSessionEntityDto = quizSessionService.startReviewSession(userId);

        // 퀴즈 문제 생성
        QuizQuestionEntityDto quizQuestionEntityDto = quizQuestionService.createQuizQuestion(userId, quizSessionEntityDto.id());

        return ApiResponse.success(new QuizSessionStartResponse(quizSessionEntityDto.id(), quizQuestionEntityDto));
    }

    /**
     * 퀴즈 답변을 채점하고 결과를 저장합니다.
     * 
     * @param userId 사용자 ID
     * @param sessionId 세션 ID
     * @param request 답변 요청
     * @return 채점 결과
     */
    public ApiResponse<QuizAnswerResponse> gradeQuizAnswer(Long userId, Long sessionId, QuizAnswerRequest request) {
        // 1. 세션이 진행 중인지 확인
        QuizSessionEntityDto session = quizSessionService.findSessionById(sessionId);
        if (session.sessionStatus() != SessionStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.QUIZ_SESSION_INVALID_STATUS);
        }
        
        // 2. 퀴즈 문제 조회
        QuizQuestionEntityDto quizQuestionDto = quizQuestionService.findQuizQuestionById(request.getQuestionId());
        
        // 3. 세션과 문제의 정합성 확인
        if (!quizQuestionDto.quizSessionId().equals(sessionId)) {
            throw new BusinessException(ErrorCode.QUIZ_QUESTION_SESSION_MISMATCH);
        }
        
        // REVIEW 세션에서만 적용
        if (session.sessionType() == SessionType.REVIEW) {
            if (quizQuestionDto.isSolved()) {
                Boolean isCorrect = learningLogService.getCorrectnessByQuizQuestionId(quizQuestionDto.id());
                if (isCorrect) {
                    // 정답을 맞춘 문제는 다시 풀 수 없음
                    throw new BusinessException(ErrorCode.QUIZ_QUESTION_ALREADY_SOLVED);
                }
                // 정답을 맞추지 못한 문제는 다시 풀 수 있음
            }
        }
        
        // 전체 정답 여부 확인
        boolean isCorrect = checkAllAnswersCorrect(quizQuestionDto, request.getAnswers());
        
        if (isCorrect) {
            // 현재 문제를 해결된 상태로 표시
            quizQuestionService.markQuizQuestionAsSolved(request.getQuestionId());
        }
        
        // 다음 문제 생성 여부 확인
        QuizQuestionEntityDto nextQuestion = null;
        boolean isSessionCompleted = false;
        
        // 세션 타입에 따른 완료 조건 확인
        if (session.sessionType() == SessionType.TODAY_SENTENCE) {
            // TODAY_SENTENCE: 3문제 풀면 무조건 완료
            Integer currentQuestionCount = quizQuestionService.countQuestionsBySessionId(quizQuestionDto.quizSessionId());
            
            if (currentQuestionCount < 3) {
                // 3문제 미만이면 다음 문제 생성
                nextQuestion = quizQuestionService.createQuizQuestion(userId, quizQuestionDto.quizSessionId());
            } else {
                // 3문제가 되면 세션 완료
                isSessionCompleted = true;
                quizSessionService.completeQuizSession(quizQuestionDto.quizSessionId());
            }
        } else if (session.sessionType() == SessionType.REVIEW) {
            // REVIEW: 모든 문제를 맞춰야 완료
            if (isCorrect) {
                // 현재 문제를 맞췄다면, 모든 문제가 맞춰졌는지 확인
                List<QuizQuestionEntityDto> questions = quizQuestionService.findQuestionsBySessionIdExcludingCurrent(quizQuestionDto.quizSessionId(), quizQuestionDto.id());
                boolean allCorrect = questions.size() == 2;
                
                for (QuizQuestionEntityDto question : questions) {
                    if (question.isSolved()) {
                        Boolean questionCorrect = learningLogService.getCorrectnessByQuizQuestionId(question.id());
                        if (!questionCorrect) {
                            allCorrect = false;
                            break;
                        }
                    }
                }
                
                if (allCorrect) {
                    // 모든 문제를 맞췄다면 세션 완료
                    isSessionCompleted = true;
                    quizSessionService.completeQuizSession(quizQuestionDto.quizSessionId());
                } else {
                    // 아직 틀린 문제가 있다면 틀린 문제 중 하나를 제공
                    if (quizQuestionDto.questionOrder() == 1) {
                        // 처음 푸는 1번 문제 틀리면 2번 문제 제공
                        nextQuestion = quizQuestionService.findNextQuestionInDb(quizQuestionDto.quizSessionId(), 2);
                        if (nextQuestion == null) {
                            nextQuestion = quizQuestionService.createQuizQuestion(userId, quizQuestionDto.quizSessionId());
                        }
                    } else if (quizQuestionDto.questionOrder() == 2) {
                        // 2번 문제 틀리면 3번 문제 제공
                        nextQuestion = quizQuestionService.findNextQuestionInDb(quizQuestionDto.quizSessionId(), 3);
                        if (nextQuestion == null) {
                            nextQuestion = quizQuestionService.createQuizQuestion(userId, quizQuestionDto.quizSessionId());
                        }
                    } else {
                        // 3번 문제 틀리면 가장 작은 ID의 틀린 문제 제공
                        nextQuestion = quizQuestionService.findNextCircularWrongQuestion(quizQuestionDto.quizSessionId(), quizQuestionDto.questionOrder());
                    }

                    // 두 번째 푸는 1번 문제 또는 2번 문제 틀리면 가장 작은 ID의 틀린 문제 제공
                    if (nextQuestion == null) {
                        // 다음 순서의 틀린 문제 X
                        // -> 1) 다음 문제는 다 맞았거나
                        // -> 다음 문제가 다 맞았다면
                        if (quizQuestionService.allSubsequentQuestionsCorrect(quizQuestionDto.quizSessionId(), quizQuestionDto.questionOrder())) {
                            // -> 자기 자신 반환
                            nextQuestion = quizQuestionDto;
                        } else {
                            // -> 2) 혹은 아직 다음 문제가 안 만들어졌거나
                            // -> 다음 문제 만들어서 반환
                            nextQuestion = quizQuestionService.createQuizQuestion(userId, quizQuestionDto.quizSessionId());
                        }
                    }
                }
            } else {
                // 틀렸음 -> 근데 그게 1번이나 2번
                // 그러면 다음 순서의 틀린 문제 있는지 확인 -> 만약에 없음 -> 다시 최소 아이디로 찾아야 함 (원형이기 때문)
                if (quizQuestionDto.questionOrder() < 3) {
                    // 다음 순서의 틀린 문제 확인
                    nextQuestion = quizQuestionService.findNextWrongQuestionAfterCurrent(quizQuestionDto.quizSessionId(), quizQuestionDto.questionOrder());
                    if (nextQuestion == null) {
                        // 다음 순서의 틀린 문제 X
                        // -> 1) 다음 문제는 다 맞았거나
                        // -> 다음 문제가 다 맞았다면
                        if (quizQuestionService.allSubsequentQuestionsCorrect(quizQuestionDto.quizSessionId(), quizQuestionDto.questionOrder())) {
                            // -> 자기 자신 반환
                            nextQuestion = quizQuestionDto;
                        } else {
                            // -> 2) 혹은 아직 다음 문제가 안 만들어졌거나
                            // -> 다음 문제 만들어서 반환
                            nextQuestion = quizQuestionService.createQuizQuestion(userId, quizQuestionDto.quizSessionId());
                        }
                    }
                } else {
                    // 틀렸음 -> 근데 그게 3번
                    // 최소 문제 찾음
                    nextQuestion = quizQuestionService.findNextCircularWrongQuestion(quizQuestionDto.quizSessionId(), quizQuestionDto.questionOrder());
                }
            }
        }
        
        // 채점 결과 생성
        QuizAnswerResponse.GradingResult gradingResult = new QuizAnswerResponse.GradingResult(
            quizQuestionDto.learningSentence().sentence(), 
            isCorrect
        );
        
        QuizAnswerResponse response = new QuizAnswerResponse(gradingResult, nextQuestion, isSessionCompleted);
        
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
                throw new BusinessException(ErrorCode.INVALID_ANSWER_FORMAT);
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
            throw new BusinessException(ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }
    
    private boolean isLastQuestion(QuizQuestionEntityDto quizQuestionDto) {
        Integer currentQuestionOrder = quizQuestionDto.questionOrder();
        Integer maxQuestionOrder = quizQuestionService.findMaxQuestionOrderBySessionId(quizQuestionDto.quizSessionId());
        return currentQuestionOrder.equals(maxQuestionOrder);
    }
    
    /**
     * 퀴즈 세션의 결과를 조회합니다.
     * 
     * @param sessionId 세션 ID
     * @param userId 사용자 ID
     * @return 세션 결과 요약
     */
    public ApiResponse<QuizSessionResultResponse> getSessionResult(Long sessionId, Long userId) {
        // 1. 세션 정보 조회
        QuizSessionEntityDto session = quizSessionService.findSessionById(sessionId);
        
        // 2. 세션의 모든 문제 조회
        List<QuizQuestionEntityDto> questions = quizQuestionService.findQuestionsBySessionId(sessionId);
        
        // 3. 결과 집계
        int correctCount = 0;
        int wrongCount = 0;
        
        for (QuizQuestionEntityDto question : questions) {
            if (question.isSolved()) {
                // LearningLogService를 통해 정답 여부 확인
                Boolean isCorrect = learningLogService.getCorrectnessByQuizQuestionId(question.id());
                if (isCorrect) {
                    correctCount++;
                } else {
                    wrongCount++;
                }
            }
        }
        
        // 4. 룰렛 사용 여부 확인 (임시로 false)
        Boolean hasUsedRoulette = false;
        
        // 5. 응답 생성
        QuizSessionResultResponse result = new QuizSessionResultResponse(
            sessionId,
            session.sessionType(),
            3, // 총 문제 수
            correctCount,
            wrongCount,
            questions.size() > 0 ? (double) correctCount / questions.size() : 0.0,
            hasUsedRoulette,
            session.startedDateTime(),
            session.completedDateTime()
        );
        
        return ApiResponse.success(result);
    }
}
