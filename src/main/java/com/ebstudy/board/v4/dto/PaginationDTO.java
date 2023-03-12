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
    // TODO: 3/4. DTO 필드에도 주석을 달아야 이 변수가 어떤 역할을 하는지 유추할 수 있다.
    int totalPostCount; // select * 로 검색된 전체 게시글 개수
    int startPage; // 페이징 버튼 출력시 출력을 시작할 페이지 번호
    int endPage; // 페이징 버튼 출력시 출력을 끝낼 페이지 번호
    Integer currentPage; // 현재 페이지 번호
    int totalPage; // 총 페이지 개수
    int startPostNumber; // DB에서 n번째부터 게시글을 가져올 때 대입하는 n값
}
