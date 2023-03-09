package com.ebstudy.board.v4.global.exception;

import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 전역에서 throw하는 CustomException을 처리
     * @param e: ErrorCode Enum Name에 따른 HttpStatus, msg
     * @return 에러 처리된 ComonApiResponseDTO
     */

    // TODO: 3/11(질문) GlobalExceptionHandler는 결국 에러가 발생할 수 있는 지점에서 커스텀 에러를 발생시키도록 직접 설정해야 하고, 모든 경우의
    //  수를 가정하여 이렇게 에러를 핸들링 할 수는 없다고 리뷰받음. --> 그렇다면 핸들링하지 않고 발생하는 기본 에러를 인터셉터로 납치해서 자동으로 처리되어 반환하도록 할 수 없을까?
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonApiResponseDTO> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();
        log.error("handleCustomException throws CustomException e: " + e);

        return new ResponseEntity<>(
                CommonApiResponseDTO.builder()
                        .success(false)
                        .status(errorCode.getHttpStatus().value())
                        .errorMessage(errorCode.getMessage())
                        .build(),
                errorCode.getHttpStatus());
    }
}
