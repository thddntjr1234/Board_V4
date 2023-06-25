package com.ebstudy.board.global.exception;

import com.ebstudy.board.dto.response.CommonApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    /**
     * 전역에서 throw하는 CustomException을 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {

        // TODO: 2023/03/12 모든 핸들러가 구체적인 에러 메세지를 반환하도록 리팩토링
        e.printStackTrace();
        log.error("handleCustomException 이 CustomException을 처리 ");

        HashMap<String, String> errorCode = new HashMap<>();
        errorCode.put("errorMessage", e.getCustomErrorCode().getErrorMessage());

        CommonApiResponseDTO<Map> commonApiResponse = new CommonApiResponseDTO<>(false, errorCode);
        return new ResponseEntity<>(commonApiResponse, e.getCustomErrorCode().getHttpStatus());
    }


    /**
     * 전역에서 throw되는 ValidationException에 대해서 에러 처리
     */
    @ExceptionHandler({ValidationException.class, AuthenticationException.class})
    public ResponseEntity<Object> handleValidationAndSecurityException(Exception e, WebRequest request) {

        log.info(String.valueOf(e.getCause()));
        HttpStatus status;

        if (e instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        HashMap<String, String> errorCode = new HashMap<>();
        errorCode.put("errorMessage", e.getMessage());

        CommonApiResponseDTO<Map<String, String>> commonApiResponse = new CommonApiResponseDTO<>(false, errorCode);

        return new ResponseEntity<>(commonApiResponse, status);
    }

    /**
     * 전역으로 throw되는 CustomException, ValidationException을 제외한 모든 Exception을 핸들링하는 메소드
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception e, WebRequest request) {

        // TODO: 기타 오류들은 해당 핸들러가 처리하므로 에러 내용들을 뽑아서 요구되는 포맷으로 변환해 반환(메소드화)

        log.info("Exception e는: " + e.toString());
        log.info("Exception e Stacktrace[0] 내용: " + e.getStackTrace()[0]);
        e.printStackTrace();
        log.info("Exception e Cause 내용: " + e.getCause());
        log.info("exception simplenname: " + e.getClass().getSimpleName());
        log.info("Exceptin getMethod: " + Arrays.toString(e.getClass().getMethods()));
        log.info("Exception Method Name: " + e.getStackTrace()[0].getMethodName());
        log.info("Exception e " + e.getMessage());
        log.info("WebRequest request 내용" + request.getDescription(true));

        HashMap<String, String> errorData = new HashMap<>();

        try {
            handleException(e, request);
        } catch (Exception ex) {
            // Spring MVC 예외가 아닌 것들은 다시 throw되는데 이걸 캐치해서 기존의 핸들러가 동작하지 못하게 한다.
            errorData.put("errorMessage", ex.getMessage());
        }

        CommonApiResponseDTO<Object> commonApiResponse = new CommonApiResponseDTO<>(false, errorData);
        log.info("handleException에서 뱉은 에러를 다시 캐치");
        return new ResponseEntity<>(commonApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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