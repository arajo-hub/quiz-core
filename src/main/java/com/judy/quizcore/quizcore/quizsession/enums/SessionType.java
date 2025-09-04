package com.judy.quizcore.quizcore.quizsession.enums;

/**
 * 퀴즈 세션 타입
 * 세션의 목적과 동작 방식을 정의합니다.
 */
public enum SessionType {
    
    /**
     * 오늘의 문장
     * 3문제를 풀면 무조건 세션 종료
     */
    TODAY_SENTENCE("오늘의 문장"),
    
    /**
     * 복습
     * 모든 문제를 맞춰야 세션 종료
     * 틀린 문제는 재시도 가능
     */
    REVIEW("복습");
    
    private final String displayName;
    
    SessionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
