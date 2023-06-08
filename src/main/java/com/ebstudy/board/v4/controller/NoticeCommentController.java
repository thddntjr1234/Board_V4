package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.service.NoticeCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NoticeCommentController {

    private final NoticeCommentService commentService;

    @PostMapping("/api/boards/notice/comment")
    public CommonApiResponseDTO<?> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @DeleteMapping("/api/boards/notice/comment/{commentId}")
    public CommonApiResponseDTO<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @PutMapping("/api/boards/notice/comment")
    public CommonApiResponseDTO<?> updateComment(@RequestBody CommentDTO comment) {
        commentService.updateComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
