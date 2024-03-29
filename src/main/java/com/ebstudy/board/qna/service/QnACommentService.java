package com.ebstudy.board.qna.service;

import com.ebstudy.board.dto.*;
import com.ebstudy.board.qna.repository.QnACommentMapper;
import com.ebstudy.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class QnACommentService {

    private final QnACommentMapper commentMapper;
    private final UserService userService;

    /**
     * 게시글 id에 해당하는 댓글 리스트를 가져오는 메소드
     * @param postId 가져올 댓글의 게시글 id
     * @return 댓글 리스트
     */
    public List<CommentDTO> getCommentList(Long postId) {
        return commentMapper.getCommentList(postId);
    }

    /**
     * 댓글 id를 사용하여 DB에서 댓글 정보를 가져온다.
     * @param commentId 가져올 댓글의 id값
     * @return 댓글 정보
     */
    public CommentDTO findCommentByCommentId(Long commentId) { return commentMapper.findCommentByCommentId(commentId); }

    /**
     * 댓글 저장 메소드
     * @param comment 저장할 댓글
     */
    public void saveComment( CommentDTO comment) {
        UserDTO user = userService.getUserFromContext();

        comment.setUserId(user.getUserId());
        comment.setCreatedDate(String.valueOf(LocalDateTime.now()));
        commentMapper.saveComment(comment);
    }

    /**
     * 댓글 삭제 메소드
     * @param commentId 삭제할 댓글의 Id
     */
    public void deleteComment(Long commentId) {
        UserDTO user = userService.getUserFromContext();

        CommentDTO originComment = commentMapper.findCommentByCommentId(commentId);

        if (user.getUserId().equals(originComment.getUserId())) {
            commentMapper.deleteComment(commentId);
        }
    }

    /**
     * 댓글 수정 메소드
     * @param comment 수정할 댓글
     */
    public void updateComment(CommentDTO comment) {
        UserDTO user = userService.getUserFromContext();

        CommentDTO originComment = commentMapper.findCommentByCommentId(comment.getCommentId());

        if (user.getUserId().equals(originComment.getUserId())) {
            comment.setModifiedDate(String.valueOf(LocalDateTime.now()));
            commentMapper.updateComment(comment);
        }
    }
}
