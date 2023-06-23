package com.ebstudy.board.v4.notice.repository;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticePostMapper {

    /**
     * Pagination Value를 사용하여 게시글 리스트 조회
     */
    List<PostDTO> getPostList(PaginationDTO paginationValues);

    /**
     * 공지사항 상단 고정 게시글 리스트 조회
     */
    List<PostDTO> getFixedPostList(String target);

    /**
     * 검색 조건에 해당하는 총 게시글 개수를 조회
     */
    int getPostCount(SearchDTO searchValues);

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
     * 해당 postId 게시글을 삭제 처리(flag 설정)
     */
    void deletePost(PostDTO post);
}
