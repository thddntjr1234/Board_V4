package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface InquiryPostMapper {
    /**
     * Pagination Value를 사용하여 게시글 리스트 조회
     */
    List<PostDTO> getPostList(@Param("paginationValues") PaginationDTO paginationValues, @Param("authorId") Long authorId);

    /**
     * 검색 조건에 해당하는 총 게시글 개수를 조회
     */
    int getPostCount(@Param("paginationValues") SearchDTO searchValues, @Param("authorId") Long authorId);

    /**
     * postId에 해당하는 게시글 조회
     */
    PostDTO getPost(Long postId);

    /**
     * 요청받은 게시글을 저장하고 postId 값을 반환
     */
    long savePost(PostDTO post);

    /**
     * 해당 postId값의 게시글 조회수를 1 증가
     */
    void increaseHits(long postId);

    /**
     * 요청받은 게시글을 수정
     */
    void updatePost(PostDTO post);

    /**
     * 채택한 댓글을 게시글 테이블에 등록
     */
    void adoptComment(PostDTO post);

    /**
     * 해당 postId 게시글을 삭제 처리(flag 설정)
     */
    void deletePost(PostDTO post);
}
