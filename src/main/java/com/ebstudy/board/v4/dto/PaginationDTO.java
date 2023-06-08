package com.ebstudy.board.v4.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PaginationDTO {

    // 페이징 필드
    // select * 로 검색된 전체 게시글 개수
    private int totalPostCount;

    // 페이징 버튼 출력시 출력을 시작할 페이지 번호
    private int startPage;

    // 페이징 버튼 출력시 출력을 끝낼 페이지 번호
    private int endPage;

    // 현재 페이지 번호
    private Integer currentPage;

    // 총 페이지 개수
    private int totalPage;

    // DB에서 n번째부터 게시글을 가져올 때 대입하는 n값
    private int startPostNumber;

    // 검색 필드
    // 검색어
    private String keyword;

    // 검색기간(시작일)
    private String startDate;

    // 검색기간(종료일)
    private String endDate;

    // 검색 카테고리
    private Integer categoryId;

    // 게시글 필터
    private String filter;

    // 비밀 게시글 제외 여부
    private String secret;

    // 정렬 방식
    private String sort;
}
