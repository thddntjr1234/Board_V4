package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDTO {

    // 입력받은 페이지 번호
    Integer pageNumber;

    // 검색 카테고리 id
    Integer categoryId;

    // 검색 키워드
    String keyword;

    // 검색기간(시작일)
    String startDate;

    // 검색기간(종료일)
    String endDate;
}
