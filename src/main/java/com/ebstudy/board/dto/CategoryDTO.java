package com.ebstudy.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {

    // 카테고리 pk값
    private Long categoryId;

    // 카테고리명
    private String category;
}
