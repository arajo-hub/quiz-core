package com.judy.quizcore.quizcore.common.exception;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직 예외를 위한 클래스
 * 애플리케이션의 비즈니스 규칙 위반 시 발생하는 예외입니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus code;
    private final ErrorCode errorCode;
    
    // 1. ErrorCode만
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }
    
    // 2. ErrorCode + Throwable
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }
    
    // 3. ErrorCode + details
    public BusinessException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + (details != null ? " - " + details : ""));
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }
    
    // 4. HttpStatus + ErrorCode
    public BusinessException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = httpStatus;
        this.errorCode = errorCode;
    }
    
    // 5. HttpStatus + ErrorCode + details
    public BusinessException(HttpStatus httpStatus, ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + (details != null ? " - " + details : ""));
        this.code = httpStatus;
        this.errorCode = errorCode;
    }
    
    // 6. ErrorCode + details + Throwable
    public BusinessException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage() + (details != null ? " - " + details : ""), cause);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }
    
    // 7. HttpStatus + ErrorCode + details + Throwable
    public BusinessException(HttpStatus httpStatus, ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage() + (details != null ? " - " + details : ""), cause);
        this.code = httpStatus;
        this.errorCode = errorCode;
    }
}
