package com.ebstudy.board.global.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorData {
    private int code; // http status 코드
    private String errorMessage; // e.getMessage()로 담기는 필드, 예외 상세 내용

    public ErrorData(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
