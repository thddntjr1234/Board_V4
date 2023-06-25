package com.ebstudy.board.notice.controller;

import com.ebstudy.board.dto.CommentDTO;
import com.ebstudy.board.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.notice.service.NoticeCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NoticeCommentController {

    private final NoticeCommentService commentService;

    /**
     * 댓글 저장을 수행
     *
     * @param comment 저장할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PostMapping("/api/boards/notice/comment")
    public CommonApiResponseDTO<?> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 댓글 삭제를 수행
     *
     * @param commentId 삭제할 댓글 ID
     * @return 성공 여부를 담은 Response 객체
     */
    @DeleteMapping("/api/boards/notice/comment/{commentId}")
    public CommonApiResponseDTO<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 댓글 수정을 수행
     *
     * @param comment 수정할 댓글 정보
     * @return 성공 여부를 담은 Response 객체
     */
    @PutMapping("/api/boards/notice/comment")
    public CommonApiResponseDTO<?> updateComment(@RequestBody CommentDTO comment) {
        commentService.updateComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}