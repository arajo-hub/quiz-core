package com.judy.quizcore.quizcore.learningsentence.entities;

import com.judy.quizcore.quizcore.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 학습문장 엔티티
 * 사용자가 학습할 문장의 기본 정보를 저장합니다.
 * 문장, 번역, 카테고리, 난이도 등의 정보를 포함합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LearningSentence extends BaseEntity {
    
    /**
     * 학습문장 고유 식별자
     * 데이터베이스에서 자동으로 생성되는 기본키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 학습할 문장 내용
     * 사용자가 학습해야 할 실제 문장입니다.
     * 최대 1000자까지 저장 가능합니다.
     */
    @Column(nullable = false, length = 1000)
    private String sentence;
    
    /**
     * 문장의 번역
     * 학습 문장의 의미를 이해하기 위한 번역문입니다.
     * 최대 500자까지 저장 가능합니다.
     */
    @Column(length = 500)
    private String translation;
}
