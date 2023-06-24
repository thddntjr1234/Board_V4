package com.ebstudy.board.notice.repository;

import com.ebstudy.board.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeCommentMapper {

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
