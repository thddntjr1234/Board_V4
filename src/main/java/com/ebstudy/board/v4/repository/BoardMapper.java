package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.FileDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // SqlSession을 만들고 Mapper를 등록할 필요가 없이 이 어노테이션으로 해결 가능
public interface BoardMapper {

    List<PostDTO> getPostList(int startPostNumber);

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

    void updatePost(PostDTO post);

    void deletePost(long postId);
}
