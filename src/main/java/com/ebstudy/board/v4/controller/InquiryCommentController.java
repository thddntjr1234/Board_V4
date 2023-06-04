package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.service.InquiryCommentService;
import com.ebstudy.board.v4.service.InquiryPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryCommentController {

    private final InquiryCommentService commentService;
    private final InquiryPostService postService;

    @PostMapping("/api/boards/inquiry/comment")
    public CommonApiResponseDTO<?> saveComment(@RequestBody CommentDTO comment) {
        commentService.saveComment(comment);

        // 게시글 카테고리 변경
        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @DeleteMapping("/api/boards/inquiry/comment/{commentId}")
    public CommonApiResponseDTO<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        // 게시글 카테고리 변경
        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @PutMapping("/api/boards/inquiry/comment")
    public CommonApiResponseDTO<?> updateComment(@RequestBody CommentDTO comment) {
        commentService.updateComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
