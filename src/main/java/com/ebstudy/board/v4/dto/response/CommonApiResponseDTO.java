package com.ebstudy.board.v4.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonApiResponseDTO<T> {

    private Boolean success; // 요청 성공 여부
    private int status; // HttpStatus 코드
    private T data; // 성공시 들어가는 데이터
    private String errorMessage; // 에러 메세지
}
