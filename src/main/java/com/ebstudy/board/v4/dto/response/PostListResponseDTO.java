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
    List<CategoryDTO> categoryList;
    PaginationDTO pagingValues;
    List<PostDTO> postList;
}
