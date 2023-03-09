package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private Long categoryId; // 카테고리 pk값
    private String category; // 카테고리명
}
