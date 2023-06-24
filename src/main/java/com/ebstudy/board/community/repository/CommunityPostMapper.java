package com.ebstudy.board.community.repository;

import com.ebstudy.board.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper // SqlSession을 만들고 Mapper를 등록할 필요가 없이 이 어노테이션으로 해결 가능
public interface CommunityPostMapper {

    /**
     * Pagination Value를 사용하여 게시글 리스트 조회
     */
    List<PostDTO> getPostList(PaginationDTO paginationValues);

    /**
     * 검색 조건에 해당하는 총 게시글 개수를 조회
     */
    int getPostCount(SearchDTO searchValues);

    /**
     * postId에 해당하는 게시글 조회
     */
    PostDTO getPost(Long postId);

    /**
     * 카테고리 리스트를 조회
     */
    List<CategoryDTO> getCategoryList();

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
     * 해당 postId 게시글을 삭제 처리(flag 설정)
     */
    void deletePost(PostDTO post);
}
