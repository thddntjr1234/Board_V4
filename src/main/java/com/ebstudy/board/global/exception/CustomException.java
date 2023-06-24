package com.ebstudy.board.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final CustomErrorCode customErrorCode;
}
