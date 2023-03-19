package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper
public interface CommentMapper {

    /**
     * 해당 postId 게시글에 종속된 댓글 리스트를 조회
     */
    List<CommentDTO> getCommentList(long postId);

    /**
     * 요청받은 게시글을 저장
     */
    void saveComment(CommentDTO comment);

    void updateComment(CommentDTO comment);
}
