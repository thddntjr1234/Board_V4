package com.ebstudy.board.v4.global.exception;

import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    /**
     * 전역에서 throw하는 CustomException을 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {

        // TODO: 2023/03/12 모든 핸들러가 구체적인 에러 메세지를 반환하도록 리팩토링
        ErrorCode errorCode = e.getErrorCode();
        e.printStackTrace();
        log.error("handleCustomException throws CustomException e: " + e);

        CommonApiResponseDTO<ErrorCode> commonApiResponse = new CommonApiResponseDTO<>(false, errorCode);
        return new ResponseEntity<>(commonApiResponse, e.getErrorCode().getHttpStatus());
    }


    /**
     * 전역에서 throw되는 ValidationException에 대해서 에러 처리
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(Exception e, WebRequest request) {

        log.info(String.valueOf(e.getCause()));
        HttpStatus status = HttpStatus.BAD_REQUEST;

        e.printStackTrace();
        ErrorData errorData = new ErrorData(status.value(), e.getMessage());
        CommonApiResponseDTO<ErrorData> commonApiResponse = new CommonApiResponseDTO<>(false, errorData);

        return new ResponseEntity<>(commonApiResponse, status);
    }


    /**
     * 전역으로 throw되는 CustomException, ValidationException을 제외한 모든 Exception을 핸들링하는 메소드
     */
    @ExceptionHandler(Exception.class)
    public void handleGlobalException(Exception e, WebRequest request) {

        // TODO: 기타 오류들은 해당 핸들러가 처리하므로 에러 내용들을 뽑아서 요구되는 포맷으로 변환해 반환

        log.info("Exception e는: " + e.toString());
        log.info("Exception e Stacktrace[0] 내용: " + e.getStackTrace()[0]);
        e.printStackTrace();
        log.info("Exception e Cause 내용: " + e.getCause());
        log.info("exception simplenname: " + e.getClass().getSimpleName());
        log.info("Exceptin getMethod: " + Arrays.toString(e.getClass().getMethods()));
        log.info("Exception Method Name: " + e.getStackTrace()[0].getMethodName());
        log.info("Exception e " + e.getMessage());
        log.info("WebRequest request 내용" + request.getDescription(true));

    }

    /**
     * handleGLobalException에 존재하던 handleException을 GlobalExceptionHandlerd에서 분리시킨 메소드
     * 전역에서 발생하는 스프링 관련 예외들을 처리
     * @param e
     */
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public void handleResponseEntityException(Exception e, WebRequest request) throws Exception {
        handleException(e, request);
    }

    /**
     * handleException에서 처리하는 Spring 관련 예외를 최종적으로 반환하는 메소드, 오버라이딩하여 원하는 메세지 형식으로 변환한다.
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        log.info("handleException이 오버라이딩된 handleExceptionInternal을 실행함");
        log.info("handleExceptionInternal이 잡은 메세지 e: " + e.getMessage());
        log.info("handleExceptionInternal이 잡은 status 코드: " + status.name());

        // status.name과 e.getMessage를 ErrorCode에 반환하도록 하면 되겠다!
        ErrorData errorData = new ErrorData(status.value(), e.getMessage());
        CommonApiResponseDTO<ErrorData> commonApiResponse = new CommonApiResponseDTO<>(false, errorData);

        return new ResponseEntity<>(commonApiResponse, status);
    }


}
