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
