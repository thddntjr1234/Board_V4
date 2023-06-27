package com.ebstudy.board.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 전역에서 throw하는 CustomException을 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {
        e.printStackTrace();

        HashMap<String, Object> error = new HashMap<>();
        error.put("errorCode", e.getCustomErrorCode().getErrorCode());
        error.put("errorMessage", e.getCustomErrorCode().getErrorMessage());

        return ResponseEntity.status(e.getCustomErrorCode().getHttpStatus()).body(error);
    }


    /**
     * 전역에서 throw되는 ValidationException에 대해서 에러 처리
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationAndSecurityException(Exception e, WebRequest request) {
        e.printStackTrace();

        HashMap<String, Object> error = new HashMap<>();
        error.put("errorCode", 40003);
        error.put("errorMessage", e.getCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 전역으로 throw되는 CustomException, ValidationException을 제외한 모든 Exception을 핸들링하는 메소드
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception e, WebRequest request) {
        e.printStackTrace();

        CustomErrorCode customErrorCode = CustomErrorCode.INTERNAL_SERVER_ERROR;

        HashMap<String, Object> error = new HashMap<>();
        error.put("errorCode", customErrorCode.getErrorCode());
        error.put("errorMessage", customErrorCode.getErrorMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
