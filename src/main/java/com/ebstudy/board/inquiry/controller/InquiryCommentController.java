package com.ebstudy.board.inquiry.controller;

import com.ebstudy.board.dto.CommentDTO;
import com.ebstudy.board.inquiry.service.InquiryCommentService;
import com.ebstudy.board.inquiry.service.InquiryPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryCommentController {

    private final InquiryCommentService commentService;
    private final InquiryPostService postService;

    /**
     * 댓글 저장을 수행
     *
     * @param comment 저장할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PostMapping("/api/boards/inquiry/comment")
    public ResponseEntity<Object> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        // 게시글 카테고리 변경
        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 삭제를 수행
     *
     * @param commentId 삭제할 댓글 ID
     * @return 성공 여부를 담은 Response 객체
     */
    @DeleteMapping("/api/boards/inquiry/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        // 게시글 카테고리 변경
        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 수정을 수행
     *
     * @param comment 수정할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PutMapping("/api/boards/inquiry/comment")
    public ResponseEntity<Object> updateComment(@RequestBody CommentDTO comment) {
        commentService.updateComment(comment);

        return ResponseEntity.ok(null);
    }
}
