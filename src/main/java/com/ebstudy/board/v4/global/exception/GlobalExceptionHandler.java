package com.ebstudy.board.v4.global.exception;

import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    /**
     * 전역에서 throw하는 CustomException을 처리
     *
     * @param e: ErrorCode Enum Name에 따른 HttpStatus, msg
     * @return 에러 처리된 ComonApiResponseDTO
     */

    // TODO: 3/11(질문) GlobalExceptionHandler는 결국 에러가 발생할 수 있는 지점에서 커스텀 에러를 발생시키도록 직접 설정해야 하고, 모든 경우의
    //  수를 가정하여 이렇게 에러를 핸들링 할 수는 없음. 결국 CustomException을 던지지 않아도 Exception이 알아서 핸들링되어야 할텐데 어떻게 해결할 수 있을까?
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {

        ErrorCode errorCode = e.getErrorCode();
        log.error("handleCustomException throws CustomException e: " + e);

        CommonApiResponseDTO<ErrorCode> commonApiResponse = new CommonApiResponseDTO<>(false, errorCode);
        return new ResponseEntity<>(commonApiResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(Exception e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorData errorData = new ErrorData(status.value(), e.getMessage());
        CommonApiResponseDTO<ErrorData> commonApiResponse = new CommonApiResponseDTO<>(false, errorData);

        return new ResponseEntity<>(commonApiResponse, status);
    }

    // TODO: e.getCause()에서 원인 메세지만 분리한 후 enum이름으로 사용해서 throw CustomException("~~")가 되게 한다면 되지 않을까?
    //  -> 발생할 수 있는 Exception에 대해서 Enum을 만들어야 하는데 이게 가능한지 모르겠다. ResponseEntityExceptionHandlerd에선 instanceOf로 체크한다.

    //  TODO: validation, global, custom으로 대부분의 exception은 걸러지겠지만 global은 ResponseEntityExceptionHandler의 handleException을 사용하기 때문에
    //   선언된 클래스에 해당하는 오류만 실제로 처리되게 된다. 이마저도 거르고 싶으면 Exception.class에서 해당 범위만큼 선언해 주고
    //   그 밑에 Exception.class를 둔 뒤 별도로 처리하도록 하는 방식으로 모든 예외를 처리할 수 있게 된다.
    @ExceptionHandler(Exception.class)
    public void handleGlobalException(Exception e, WebRequest request) throws Exception {

        log.info("handleGlobalExceptiond에서 전역으로 에러를 캐치함");
        log.info("ResponseEntityExceptionHanlder에서 handleException을 불러서 해당하는 status 부여");

        handleException(e, request);
//        log.info(String.valueOf(e.getCause()));
    }

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
