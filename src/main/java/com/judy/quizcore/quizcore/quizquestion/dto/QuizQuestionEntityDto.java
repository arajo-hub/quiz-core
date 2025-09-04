package com.judy.quizcore.quizcore.quizquestion.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judy.quizcore.quizcore.quizquestion.entities.QuizQuestion;
import com.judy.quizcore.quizcore.quizquestion.enums.QuestionType;
import com.judy.quizcore.quizcore.learningsentence.dto.LearningSentenceEntityDto;

import java.time.LocalDateTime;

/**
 * 퀴즈 문제 DTO
 * 퀴즈 문제 정보를 전달하기 위한 데이터 전송 객체입니다.
 */
public record QuizQuestionEntityDto(
    Long id,
    Long userId,
    Long quizSessionId,
    LearningSentenceEntityDto learningSentence,
    Integer questionOrder,
    QuestionType questionType,
    Object question,
    Object options
) {
    
    /**
     * QuizQuestion 엔티티를 DTO로 변환합니다.
     * @param quizQuestion 변환할 엔티티
     * @return 변환된 DTO
     */
    public static QuizQuestionEntityDto from(QuizQuestion quizQuestion) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        Object questionObject = null;
        Object optionsObject = null;
        
        try {
            // JSON 문자열을 객체로 파싱
            if (quizQuestion.getQuestion() != null) {
                questionObject = objectMapper.readValue(quizQuestion.getQuestion(), Object.class);
            }
            if (quizQuestion.getOptions() != null) {
                optionsObject = objectMapper.readValue(quizQuestion.getOptions(), Object.class);
            }
        } catch (JsonProcessingException e) {
            // 파싱 실패 시 원본 문자열 사용
            questionObject = quizQuestion.getQuestion();
            optionsObject = quizQuestion.getOptions();
        }
        
        return new QuizQuestionEntityDto(
                quizQuestion.getId(),
                quizQuestion.getUserId(),
                quizQuestion.getQuizSession() != null ? quizQuestion.getQuizSession().getId() : null,
                quizQuestion.getLearningSentence() != null ? 
                    LearningSentenceEntityDto.from(quizQuestion.getLearningSentence()) : null,
                quizQuestion.getQuestionOrder(),
                quizQuestion.getQuestionType(),
                questionObject,
                optionsObject
        );
    }
}
