package com.judy.quizcore.quizcore.common.exception;

import lombok.Getter;

/**
 * 접근이 거부되었을 때 발생하는 예외
 * 권한은 있지만 특정 리소스에 접근할 수 없을 때 사용됩니다.
 */
@Getter
public class AccessDeniedException extends RuntimeException {
    
    private final String resource;
    
    public AccessDeniedException(String message) {
        super(message);
        this.resource = null;
    }
    
    public AccessDeniedException(String message, String resource) {
        super(message);
        this.resource = resource;
    }
}
