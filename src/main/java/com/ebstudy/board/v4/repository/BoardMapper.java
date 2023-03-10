package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@Mapper // SqlSession을 만들고 Mapper를 등록할 필요가 없이 이 어노테이션으로 해결 가능
public interface BoardMapper {

    List<PostDTO> getPostList(PaginationDTO paginationValues);

    int getPostCount();

    PostDTO getPost(Long postId);

    List<CategoryDTO> getCategoryList();

    long savePost(PostDTO post);

    void increaseHits(long postId);

    List<FileDTO> getFileList(long postId);

    boolean checkFileExistence(long postId);

    void saveFile(FileDTO file);

    void updateFile(FileDTO file);

    List<CommentDTO> getCommentList(long postId);

    void saveComment(CommentDTO comment);

    void updateComment(CommentDTO comment);

    void updatePost(@EqualEachPasswd({"passwd", "confirmPasswd"}) PostDTO post);

    void deletePost(long postId);
}
