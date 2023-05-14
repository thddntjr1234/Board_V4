package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO {

    // 댓글 PK값
    private Long commentId;

    // 소속된 게시글의 pk값
    private Long postId;

    // 댓글 작성자
    private Long userId;

    private String username;

    // 생성일
    private String createdDate;

    // 수정일
    private String modifiedDate;

    // 댓글 내용
    private String comment;
}
