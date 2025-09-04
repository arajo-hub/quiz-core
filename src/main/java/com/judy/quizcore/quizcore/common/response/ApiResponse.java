package com.judy.quizcore.quizcore.common.response;

import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * API 응답의 기본 구조를 정의하는 클래스
 * 모든 API 응답에서 공통으로 사용되는 형식을 제공합니다.
 * 
 * @param <T> 응답 데이터의 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    
    /**
     * 상태 코드
     */
    private int code;
    
    /**
     * 응답 메시지
     */
    private String message;
    
    /**
     * 응답 데이터
     */
    private T data;
    
    /**
     * 성공 응답을 생성하는 정적 메서드
     * 
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }
    
    /**
     * 성공 응답을 생성하는 정적 메서드 (메시지 포함)
     * 
     * @param data 응답 데이터
     * @param message 성공 메시지
     * @param <T> 데이터 타입
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * 실패 응답을 생성하는 정적 메서드
     * 
     * @param message 실패 메시지
     * @param <T> 데이터 타입
     * @return 실패 응답 객체
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .code(400)
                .message(message)
                .data(null)
                .build();
    }
    
    /**
     * 실패 응답을 생성하는 정적 메서드 (데이터 포함)
     * 
     * @param message 실패 메시지
     * @param data 에러 관련 데이터
     * @param <T> 데이터 타입
     * @return 실패 응답 객체
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .code(400)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * ResponseCode를 사용한 성공 응답 생성
     * 
     * @param errorCode 응답 코드
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(ErrorCode errorCode, T data) {
        return ApiResponse.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }
    
    /**
     * ResponseCode를 사용한 에러 응답 생성
     * 
     * @param errorCode 응답 코드
     * @param <T> 데이터 타입
     * @return 에러 응답 객체
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }
}
