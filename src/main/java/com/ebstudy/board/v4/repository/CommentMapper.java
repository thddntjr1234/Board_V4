package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper
public interface CommentMapper {
    List<CommentDTO> getCommentList(long postId);

    void saveComment(CommentDTO comment);

    void updateComment(CommentDTO comment);
}
