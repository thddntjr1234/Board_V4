package com.ebstudy.board.v4.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonApiResponseDTO<T> {

    private Boolean success;
    private int status;
    private T data;
    private String errorMessage;
}
