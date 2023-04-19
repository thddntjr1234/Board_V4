package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/boards/comment")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CommonApiResponseDTO<?> saveComment(@ModelAttribute CommentDTO comment) {

        commentService.saveComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
