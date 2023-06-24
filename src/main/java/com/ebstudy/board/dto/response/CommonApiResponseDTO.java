package com.ebstudy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommonApiResponseDTO<T> {

    private Boolean success; // 요청 성공 여부
    private T data; // 성공/실패시 들어가는 데이터
}
