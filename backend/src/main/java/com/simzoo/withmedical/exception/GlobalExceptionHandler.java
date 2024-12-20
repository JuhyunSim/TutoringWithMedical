package com.simzoo.withmedical.exception;

import com.simzoo.withmedical.dto.exception.CustomErrorResponseDto;
import com.simzoo.withmedical.dto.exception.NotValidResponseDto;
import com.simzoo.withmedical.dto.exception.UnexpectedErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 검증에서 실패했을 때 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NotValidResponseDto> handleNotValidException(
        MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        NotValidResponseDto response = new NotValidResponseDto();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            response.addErrorMessage(NotValidResponseDto.Message.builder()
                .message(fieldError.getDefaultMessage())
                .field("Invalid value for field: " + fieldError.getField())
                .build());
        }

        // 클래스 레벨 에러 처리
        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            response.addErrorMessage(NotValidResponseDto.Message.builder()
                .message(globalError.getDefaultMessage())
                .field("Invalid value for field: " + globalError.getObjectName())
                .build());
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    // CustomException 예외 처리 핸들러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponseDto> handleCustomException(CustomException ex) {
        log.error("CustomException handled: {}", ex.getErrorCode());

        ErrorCode errorCode = ex.getErrorCode();
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
            errorCode.name(),
            errorCode.getMessage()
        );
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(errorResponse);
    }

    // 예기치 않은 모든 예외를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UnexpectedErrorResponseDto> handleUnexpectedException(Exception e) {
        // 에러 로그를 기록
        log.error("Unexpected error occurred: {}", e);

        // UnexpectedErrorResponseDto를 통해 클라이언트에게 오류 메시지 반환
        String message = "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.";
        String errorCode = HttpStatus.INTERNAL_SERVER_ERROR.name();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        UnexpectedErrorResponseDto response = UnexpectedErrorResponseDto.of(message, errorCode, statusCode);

        // 500 Internal Server Error 상태로 응답을 반환
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }


}

