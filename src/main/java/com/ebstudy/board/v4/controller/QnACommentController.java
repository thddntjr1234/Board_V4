package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.service.QnACommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QnACommentController {
    private final QnACommentService qnaCommentService;

    @PostMapping("/api/boards/qna/comment")
    public CommonApiResponseDTO<?> saveComment(@RequestBody CommentDTO comment) {
        qnaCommentService.saveComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @DeleteMapping("/api/boards/qna/comment/{commentId}")
    public CommonApiResponseDTO<?> deleteComment(@PathVariable Long commentId) {
        qnaCommentService.deleteComment(commentId);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    @PutMapping("/api/boards/qna/comment")
    public CommonApiResponseDTO<?> updateComment(@RequestBody CommentDTO comment) {
        qnaCommentService.updateComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
