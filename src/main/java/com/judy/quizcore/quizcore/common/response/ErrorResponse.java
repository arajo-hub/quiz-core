package com.judy.quizcore.quizcore.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.judy.quizcore.quizcore.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * 에러 응답을 위한 클래스
 * API에서 발생하는 에러 정보를 체계적으로 전달하기 위한 클래스입니다.
 * 
 * @param <T> 에러 데이터의 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class ErrorResponse<T> {
    
    /**
     * HTTP 상태 코드
     */
    private int code;
    
    /**
     * 에러 코드
     */
    private int errorCode;
    
    /**
     * 에러 메시지
     */
    private String message;
    
    /**
     * 에러 관련 데이터
     */
    @JsonInclude(NON_NULL)
    private T data;
    
    /**
     * 기본 에러 응답을 생성하는 정적 메서드
     * 
     * @param code HTTP 상태 코드
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return ErrorResponse 객체
     */
    public static <T> ErrorResponse<T> of(HttpStatus code, ErrorCode errorCode) {
        return ErrorResponse.<T>builder()
                .code(code.value())
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    /**
     * 기본 에러 응답을 생성하는 정적 메서드
     *
     * @param code HTTP 상태 코드
     * @param errorCode 에러 코드
     * @param data 에러 관련 데이터
     * @param <T> 데이터 타입
     * @return ErrorResponse 객체
     */
    public static <T> ErrorResponse<T> of(HttpStatus code, ErrorCode errorCode, T data) {
        return ErrorResponse.<T>builder()
                .code(code.value())
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }
}
