package com.ebstudy.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {

    // 입력받은 페이지 번호
    private Integer pageNumber;

    // 검색 카테고리 id
    private Integer categoryId;

    // 검색 키워드
    private String keyword;

    // 검색기간(시작일)
    private String startDate;

    // 검색기간(종료일)
    private String endDate;

    // 게시글 필터
    private String filter;

    // 비밀 게시글 제외 여부
    private String secret;

    // 정렬 방식
    private String sort;
}
