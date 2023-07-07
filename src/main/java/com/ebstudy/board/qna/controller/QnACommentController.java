package com.ebstudy.board.qna.controller;

import com.ebstudy.board.dto.CommentDTO;
import com.ebstudy.board.dto.PostDTO;
import com.ebstudy.board.global.exception.CustomErrorCode;
import com.ebstudy.board.global.exception.CustomException;
import com.ebstudy.board.qna.service.QnACommentService;
import com.ebstudy.board.qna.service.QnAPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QnACommentController {

    private final QnACommentService commentService;
    private final QnAPostService postService;

    /**
     * 댓글 저장을 수행
     *
     * @param comment 저장할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PostMapping("/api/boards/qna/comment")
    public ResponseEntity<Object> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 삭제를 수행
     *
     * @param commentId 삭제할 댓글 ID
     * @return 성공 여부를 담은 Response 객체
     */
    @DeleteMapping("/api/boards/qna/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        CommentDTO originComment = commentService.findCommentByCommentId(commentId);

        PostDTO post = postService.getPost(originComment.getPostId());
        if (post.getAdoptedCommentId() != null) {
            throw new CustomException(CustomErrorCode.INVALID_COMMENT_DELETE);
        }

        commentService.deleteComment(commentId);

        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 정보를 조회 후 해당 정보의 게시글 정보를
     *
     * @param comment 수정할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PutMapping("/api/boards/qna/comment")
    public ResponseEntity<Object> updateComment(@RequestBody CommentDTO comment) {
        CommentDTO originComment = commentService.findCommentByCommentId(comment.getCommentId());

        PostDTO post = postService.getPost(originComment.getPostId());
        if (post.getAdoptedCommentId() != null) {
            throw new CustomException(CustomErrorCode.INVALID_COMMENT_UPDATE);
        }

        commentService.updateComment(comment);

        return ResponseEntity.ok(null);
    }
}
