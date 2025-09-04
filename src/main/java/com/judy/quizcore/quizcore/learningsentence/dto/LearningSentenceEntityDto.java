package com.judy.quizcore.quizcore.learningsentence.dto;

import com.judy.quizcore.quizcore.learningsentence.entities.LearningSentence;

import java.time.LocalDateTime;

/**
 * 학습문장 DTO
 * 학습문장 정보를 전달하기 위한 데이터 전송 객체입니다.
 */
public record LearningSentenceEntityDto(
    Long id,
    String sentence,
    String translation
) {
    
    /**
     * LearningSentence 엔티티를 DTO로 변환합니다.
     * @param learningSentence 변환할 엔티티
     * @return 변환된 DTO
     */
    public static LearningSentenceEntityDto from(LearningSentence learningSentence) {
        return new LearningSentenceEntityDto(
                learningSentence.getId(),
                learningSentence.getSentence(),
                learningSentence.getTranslation()
        );
    }
}
