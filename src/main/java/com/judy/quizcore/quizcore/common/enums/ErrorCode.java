package com.judy.quizcore.quizcore.common.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 애플리케이션에서 사용하는 에러 코드를 정의하는 enum
 * 비즈니스 로직별로 체계적인 에러 코드를 제공합니다.
 * 
 * 에러 코드 체계:
 * - 1000번대: 공통 에러
 * - 2000번대: 퀴즈 세션 관련 에러
 * - 3000번대: 학습 문장 관련 에러
 * - 4000번대: 퀴즈 문제 관련 에러
 * - 5000번대: 사용자 관련 에러
 * - 6000번대: 답변 관련 에러
 * - 7000번대: 학습 진행 관련 에러
 * - 8000번대: 파일 관련 에러
 * - 9000번대: 시스템 관련 에러
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // 1000번대: 공통 에러 코드
    INVALID_PARAMETER(1000, "유효하지 않은 파라미터입니다."),
    UNAUTHORIZED(1001, "인증이 필요합니다."),
    FORBIDDEN(1002, "접근이 거부되었습니다."),
    NOT_FOUND(1003, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(1004, "허용되지 않는 HTTP 메서드입니다."),
    CONFLICT(1005, "리소스 충돌이 발생했습니다."),
    UNPROCESSABLE_ENTITY(1006, "요청은 유효하지만 처리할 수 없습니다."),
    INVALID_REQUEST(1007, "유효하지 않은 요청입니다."),
    INTERNAL_SERVER_ERROR(1008, "서버 내부 오류가 발생했습니다."),

    // 2000번대: 퀴즈 세션 관련 에러 코드
    QUIZ_SESSION_NOT_FOUND(2000, "퀴즈 세션을 찾을 수 없습니다."),
    QUIZ_SESSION_ALREADY_COMPLETED(2001, "이미 완료된 퀴즈 세션입니다."),
    QUIZ_SESSION_EXPIRED(2002, "만료된 퀴즈 세션입니다."),
    QUIZ_SESSION_IN_PROGRESS(2003, "진행 중인 퀴즈 세션이 있습니다."),
    QUIZ_SESSION_LIMIT_EXCEEDED(2004, "퀴즈 세션 생성 한도를 초과했습니다."),
    QUIZ_SESSION_INVALID_STATUS(2005, "잘못된 퀴즈 세션 상태입니다."),
    
    // 3000번대: 학습 문장 관련 에러 코드
    LEARNING_SENTENCE_NOT_FOUND(3000, "학습 문장을 찾을 수 없습니다."),
    LEARNING_SENTENCE_ALREADY_EXISTS(3001, "이미 존재하는 학습 문장입니다."),
    LEARNING_SENTENCE_INVALID_FORMAT(3002, "잘못된 학습 문장 형식입니다."),
    LEARNING_SENTENCE_TOO_LONG(3003, "학습 문장이 너무 깁니다."),
    LEARNING_SENTENCE_CATEGORY_NOT_FOUND(3004, "학습 문장 카테고리를 찾을 수 없습니다."),
    
    // 4000번대: 퀴즈 문제 관련 에러 코드
    QUIZ_QUESTION_NOT_FOUND(4000, "퀴즈 문제를 찾을 수 없습니다."),
    QUIZ_QUESTION_ALREADY_ANSWERED(4001, "이미 답변한 문제입니다."),
    QUIZ_QUESTION_ALREADY_SOLVED(4002, "이미 푼 문제입니다."),
    QUIZ_QUESTION_INVALID_TYPE(4003, "잘못된 문제 유형입니다."),
    QUIZ_QUESTION_OPTIONS_INVALID(4004, "문제 보기가 잘못되었습니다."),
    QUIZ_QUESTION_ORDER_INVALID(4005, "잘못된 문제 순서입니다."),
    
    // 5000번대: 사용자 관련 에러 코드
    USER_NOT_FOUND(5000, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(5001, "이미 존재하는 사용자입니다."),
    USER_INVALID_CREDENTIALS(5002, "잘못된 사용자 인증 정보입니다."),
    USER_ACCOUNT_LOCKED(5003, "계정이 잠겨있습니다."),
    USER_PERMISSION_DENIED(5004, "사용자 권한이 부족합니다."),
    
    // 6000번대: 답변 관련 에러 코드
    INVALID_ANSWER_FORMAT(6000, "잘못된 답변 형식입니다."),
    QUIZ_TIME_EXPIRED(6001, "퀴즈 시간이 만료되었습니다."),
    ANSWER_ALREADY_SUBMITTED(6002, "이미 제출된 답변입니다."),
    ANSWER_TOO_LONG(6003, "답변이 너무 깁니다."),
    ANSWER_REQUIRED(6004, "답변이 필요합니다."),
    
    // 7000번대: 학습 진행 관련 에러 코드
    LEARNING_PROGRESS_NOT_FOUND(7000, "학습 진행 상황을 찾을 수 없습니다."),
    INVALID_DIFFICULTY_LEVEL(7001, "잘못된 난이도 레벨입니다."),
    LEARNING_PROGRESS_ALREADY_EXISTS(7002, "이미 존재하는 학습 진행 상황입니다."),
    LEARNING_PROGRESS_INVALID_STATE(7003, "잘못된 학습 진행 상태입니다."),
    
    // 8000번대: 파일 관련 에러 코드
    FILE_NOT_FOUND(8000, "파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(8001, "파일 업로드에 실패했습니다."),
    INVALID_FILE_FORMAT(8002, "지원하지 않는 파일 형식입니다."),
    FILE_TOO_LARGE(8003, "파일 크기가 너무 큽니다."),
    FILE_CORRUPTED(8004, "파일이 손상되었습니다."),
    
    // 9000번대: 시스템 관련 에러 코드
    DATABASE_ERROR(9000, "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(9001, "외부 API 호출 중 오류가 발생했습니다."),
    CACHE_ERROR(9002, "캐시 오류가 발생했습니다."),
    NETWORK_ERROR(9003, "네트워크 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(9004, "서비스를 사용할 수 없습니다.");
    
    private final int code;
    private final String message;
    
    /**
     * 에러 코드로 ErrorCode를 찾는 메서드
     * 
     * @param code 에러 코드
     * @return ErrorCode 객체, 없으면 null
     */
    public static ErrorCode findByCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}
