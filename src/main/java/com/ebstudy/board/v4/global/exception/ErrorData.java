package com.ebstudy.board.v4.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorData {
    private int status; // http status 코드
    private String errorMessage; // e.getMessage()로 담기는 필드, 예외 상세 내용

    public ErrorData(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
