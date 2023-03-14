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
    int totalPostCount; // select * 로 검색된 전체 게시글 개수
    int startPage; // 페이징 버튼 출력시 출력을 시작할 페이지 번호
    int endPage; // 페이징 버튼 출력시 출력을 끝낼 페이지 번호
    Integer currentPage; // 현재 페이지 번호
    int totalPage; // 총 페이지 개수
    int startPostNumber; // DB에서 n번째부터 게시글을 가져올 때 대입하는 n값

    // 검색 필드
    String keyword; // 검색어
    String startDate; // 검색기간(시작일)
    String endDate; // 검색기간(종료일)
    Integer categoryId; // 검색 카테고리
}
