package com.judy.quizcore.quizcore.quizquestion.enums;

/**
 * 퀴즈 문제 유형을 정의하는 enum
 * 다양한 형태의 문제 출제 방식을 지원합니다.
 */
public enum QuestionType {
    
    /**
     * 번역 문제
     * 주어진 문장을 다른 언어로 번역하는 문제
     */
    TRANSLATION("번역"),
    
    /**
     * 빈칸 채우기
     * 문장의 일부가 비어있고 이를 채우는 문제
     */
    COMPLETION("빈칸채우기"),
    
    /**
     * 객관식 문제
     * 여러 선택지 중에서 정답을 고르는 문제
     */
    MULTIPLE_CHOICE("객관식"),
    
    /**
     * 주관식 문제
     * 자유롭게 답변을 작성하는 문제
     */
    SUBJECTIVE("주관식"),
    
    /**
     * 문장 순서 맞추기
     * 단어나 문장의 순서를 올바르게 배열하는 문제
     */
    REORDER("순서맞추기"),
    
    /**
     * 듣기 문제
     * 음성을 듣고 답변하는 문제
     */
    LISTENING("듣기"),
    
    /**
     * 문법 문제
     * 문법 규칙을 적용하는 문제
     */
    GRAMMAR("문법");
    
    private final String displayName;
    
    QuestionType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * 화면에 표시할 한글 이름을 반환합니다.
     */
    public String getDisplayName() {
        return displayName;
    }
}
