package com.judy.quizcore.quizcore.learningsentence.service;

import com.judy.quizcore.quizcore.learningsentence.dto.LearningSentenceEntityDto;
import com.judy.quizcore.quizcore.learningsentence.entities.LearningSentence;
import com.judy.quizcore.quizcore.learningsentence.repository.LearningSentenceJpaRepository;
import com.judy.quizcore.quizcore.common.exception.BusinessException;
import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class LearningSentenceService {

    private final LearningSentenceJpaRepository learningSentenceJpaRepository;

    /**
     * 랜덤으로 학습문장 하나를 가져옵니다.
     * @return 랜덤 학습문장 DTO
     * @throws BusinessException 학습문장이 없을 경우
     */
    public LearningSentenceEntityDto findRandomLearningSentence() {
        LearningSentence learningSentence = learningSentenceJpaRepository
                .findRandomLearningSentence()
                .orElseThrow(() -> new BusinessException(ErrorCode.LEARNING_SENTENCE_NOT_FOUND));
        
        return LearningSentenceEntityDto.from(learningSentence);
    }
}
