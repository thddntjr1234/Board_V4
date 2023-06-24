package com.ebstudy.board.global.exception;

import lombok.Getter;
import lombok.Setter;

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
