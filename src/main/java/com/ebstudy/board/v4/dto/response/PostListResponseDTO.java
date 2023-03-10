package com.ebstudy.board.v4.dto.response;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class PostListResponseDTO {
    List<CategoryDTO> categoryList; // 카테고리 리스트
    PaginationDTO pagingValues; // 검색어로 검색된 페이징 값
    List<PostDTO> postList; // 검색어로 검색된 게시글 리스트
}
