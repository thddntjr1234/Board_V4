package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO {

    // 소속된 게시글의 pk값
    private Long postId;

    // 생성일
    private String createdDate;

    // 댓글 내용
    private String comment;
}
