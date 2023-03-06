package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CommentDTO {
    private Long postId; // 소속된 게시글의 pk값
    private String createdDate; // 생성일
    private String comment; // 댓글 내용
}
