package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper
public interface QnACommentMapper {

    /**
     * 해당 postId 게시글에 종속된 댓글 리스트를 조회
     */
    List<CommentDTO> getCommentList(long postId);

    /**
     * 요청받은 게시글을 저장
     */
    void saveComment(CommentDTO comment);

    /**
     * 요청받은 게시글을 삭제
     */
    void deleteComment(Long commentId);

    /**
     * 요청받은 게시글을 수정
     */
    void updateComment(CommentDTO comment);

    /**
     * commentId값의 댓글을 반환
     */
    CommentDTO findCommentByCommentId(long commentId);
}
