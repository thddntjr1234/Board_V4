package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/boards/free")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public CommonApiResponseDTO<?> saveComment(@ModelAttribute CommentDTO comment) {

        commentService.saveComment(comment);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
