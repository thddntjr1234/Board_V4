package com.ebstudy.board.gallery.controller;

import com.ebstudy.board.dto.CommentDTO;
import com.ebstudy.board.gallery.service.GalleryCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GalleryCommentController {

    private final GalleryCommentService commentService;

    /**
     * 댓글 저장을 수행
     *
     * @param comment 저장할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PostMapping("/api/boards/gallery/comment")
    public ResponseEntity saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 삭제를 수행
     *
     * @param commentId 삭제할 댓글 ID
     * @return 성공 여부를 담은 Response 객체
     */
    @DeleteMapping("/api/boards/gallery/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok(null);
    }

    /**
     * 댓글 수정을 수행
     *
     * @param comment 수정할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PutMapping("/api/boards/gallery/comment")
    public ResponseEntity updateComment(@RequestBody CommentDTO comment) {
        commentService.updateComment(comment);

        return ResponseEntity.ok(null);
    }


    @GetMapping("/api/test")
    public ResponseEntity test() {
        throw new AsyncRequestTimeoutException();
    }
}
