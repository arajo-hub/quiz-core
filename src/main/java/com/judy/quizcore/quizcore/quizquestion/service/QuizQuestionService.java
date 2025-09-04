package com.judy.quizcore.quizcore.quizquestion.service;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.learninglog.service.LearningLogService;
import com.judy.quizcore.quizcore.learningsentence.dto.LearningSentenceEntityDto;
import com.judy.quizcore.quizcore.learningsentence.service.LearningSentenceService;
import com.judy.quizcore.quizcore.quizquestion.dto.QuizQuestionEntityDto;
import com.judy.quizcore.quizcore.quizquestion.entities.QuizQuestion;
import com.judy.quizcore.quizcore.quizquestion.repository.QuizQuestionJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class QuizQuestionService {

    private final LearningSentenceService learningSentenceService;
    private final LearningLogService learningLogService;
    private final QuizQuestionJpaRepository quizQuestionJpaRepository;

    /**
     * 새로운 퀴즈 문제를 생성합니다.
     * 랜덤 학습문장을 가져와서 빈칸 뚫기 문제를 만듭니다.
     * 
     * @param userId 사용자 ID
     * @param sessionId 퀴즈 세션 ID
     * @return 생성된 퀴즈 문제 DTO
     */
    public QuizQuestionEntityDto createQuizQuestion(Long userId, Long sessionId) {
        // 랜덤 학습문장 가져오기
        LearningSentenceEntityDto learningSentenceEntityDto = learningSentenceService.findRandomLearningSentence();
        
        // sessionId로 기존 문제들의 questionOrder 조회하여 +1 계산
        Integer nextQuestionOrder = calculateNextQuestionOrder(sessionId);
        
        // 퀴즈 문제 생성
        QuizQuestion quizQuestion = QuizQuestion.of(userId, sessionId, nextQuestionOrder, learningSentenceEntityDto);
        
        // 데이터베이스에 저장
        QuizQuestion savedQuizQuestion = quizQuestionJpaRepository.save(quizQuestion);
        
        // DTO로 변환하여 반환
        return QuizQuestionEntityDto.from(savedQuizQuestion);
    }
    
    /**
     * 다음 문제 순서를 계산합니다.
     * 
     * @param sessionId 퀴즈 세션 ID
     * @return 다음 문제 순서
     */
    private Integer calculateNextQuestionOrder(Long sessionId) {
        // 해당 세션의 가장 큰 questionOrder를 찾아서 +1
        Integer maxOrder = quizQuestionJpaRepository.findMaxQuestionOrderBySessionId(sessionId);
        return maxOrder != null ? maxOrder + 1 : 1;
    }
    
    /**
     * ID로 퀴즈 문제를 조회합니다.
     * 
     * @param questionId 퀴즈 문제 ID
     * @return 퀴즈 문제 DTO
     */
    public QuizQuestionEntityDto findQuizQuestionById(Long questionId) {
        QuizQuestion quizQuestion = quizQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_QUESTION_NOT_FOUND));
        return QuizQuestionEntityDto.from(quizQuestion);
    }
    
    /**
     * 채점을 위한 퀴즈 문제 정보를 조회합니다.
     * 
     * @param questionId 퀴즈 문제 ID
     * @return 채점에 필요한 정보 (ID, correctAnswer, learningSentence)
     */
    public QuizQuestion findQuizQuestionForGrading(Long questionId) {
        return quizQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_QUESTION_NOT_FOUND));
    }
    
    /**
     * 퀴즈 문제를 해결된 상태로 표시합니다.
     */
    @Transactional
    public void markQuizQuestionAsSolved(Long questionId) {
        QuizQuestion quizQuestion = quizQuestionJpaRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_QUESTION_NOT_FOUND));
        quizQuestion.markAsSolved();
    }
    
    /**
     * 특정 세션의 문제 수를 카운트합니다.
     */
    public Integer countQuestionsBySessionId(Long sessionId) {
        return quizQuestionJpaRepository.countByQuizSessionId(sessionId);
    }
    
    /**
     * 특정 세션의 모든 문제를 조회합니다.
     */
    public List<QuizQuestionEntityDto> findQuestionsBySessionId(Long sessionId) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        return questions.stream()
                .map(QuizQuestionEntityDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 세션에서 틀린 문제 중 하나를 찾습니다.
     * REVIEW 타입 세션에서 틀린 문제를 재시도할 때 사용합니다.
     */
    public QuizQuestionEntityDto findNextWrongQuestion(Long sessionId) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        
        for (QuizQuestion question : questions) {
            if (question.isSolved()) {
                // LearningLogService를 통해 정답 여부 확인
                Boolean isCorrect = learningLogService.getCorrectnessByQuizQuestionId(question.getId());
                if (!isCorrect) {
                    // 틀린 문제를 찾았으면 DTO로 변환하여 반환
                    return QuizQuestionEntityDto.from(question);
                }
            }
        }
        
        // 모든 문제를 맞췄다면 null 반환 (세션 완료)
        return null;
    }

    public QuizQuestionEntityDto findFirstWrongQuestion(Long sessionId) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);

        for (QuizQuestion question : questions) {
            if (question.isSolved()) {
                Boolean isCorrect = learningLogService.getCorrectnessByQuizQuestionId(question.getId());
                if (!isCorrect) {
                    return QuizQuestionEntityDto.from(question);
                }
            }
        }
        return null;
    }

    public Integer findMaxQuestionOrderBySessionId(Long sessionId) {
        return quizQuestionJpaRepository.findMaxQuestionOrderBySessionId(sessionId);
    }

    public QuizQuestionEntityDto findNextQuestion(Long sessionId, Integer currentOrder) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);

        for (QuizQuestion question : questions) {
            if (question.getQuestionOrder() > currentOrder) {
                return QuizQuestionEntityDto.from(question);
            }
        }
        return null;
    }

    public QuizQuestionEntityDto findNextQuestionInDb(Long sessionId, Integer currentOrder) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);

        for (QuizQuestion question : questions) {
            if (question.getQuestionOrder() == currentOrder) {
                return QuizQuestionEntityDto.from(question);
            }
        }
        return null;
    }

    public QuizQuestionEntityDto findNextCircularWrongQuestion(Long sessionId, Integer currentOrder) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        questions.removeIf(question -> question.getQuestionOrder().equals(currentOrder));
        QuizQuestionEntityDto smallestWrongQuestion = null;

        for (QuizQuestion question : questions) {
            if (!learningLogService.getCorrectnessByQuizQuestionId(question.getId())) {
                if (smallestWrongQuestion == null || question.getId() < smallestWrongQuestion.id()) {
                    smallestWrongQuestion = QuizQuestionEntityDto.from(question);
                }
            }
        }

        // If no wrong question is found, repeat the current question
        return smallestWrongQuestion != null ? smallestWrongQuestion : QuizQuestionEntityDto.from(questions.get(currentOrder - 1));
    }

    public QuizQuestionEntityDto findNextWrongQuestionAfterCurrent(Long sessionId, Integer currentOrder) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        boolean foundCurrent = false;

        for (QuizQuestion question : questions) {
            if (foundCurrent && !learningLogService.getCorrectnessByQuizQuestionId(question.getId())) {
                return QuizQuestionEntityDto.from(question);
            }
            if (question.getQuestionOrder().equals(currentOrder)) {
                foundCurrent = true;
            }
        }

        return null;
    }

    public boolean allSubsequentQuestionsCorrect(Long sessionId, Integer currentOrder) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        questions.removeIf(question -> question.getQuestionOrder().equals(currentOrder));

        if (questions.isEmpty()) {
            return false;
        }

        for (QuizQuestion question : questions) {
            if (!learningLogService.getCorrectnessByQuizQuestionId(question.getId())) {
                return false;
            }
        }

        return true;
    }

    public List<QuizQuestionEntityDto> findQuestionsBySessionIdExcludingCurrent(Long sessionId, Long currentQuestionId) {
        List<QuizQuestion> questions = quizQuestionJpaRepository.findByQuizSessionIdOrderByQuestionOrder(sessionId);
        questions.removeIf(question -> question.getId().equals(currentQuestionId));
        return questions.stream()
                .map(QuizQuestionEntityDto::from)
                .collect(Collectors.toList());
    }

    public QuizQuestionEntityDto findNextUnsolvedQuestion(List<QuizQuestionEntityDto> questions) {
        for (QuizQuestionEntityDto question : questions) {
            if (!question.isSolved()) {
                return question;
            }
        }
        return null;
    }
}
