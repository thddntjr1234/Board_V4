package com.ebstudy.board.gallery.repository;

import com.ebstudy.board.dto.PaginationDTO;
import com.ebstudy.board.dto.PostDTO;
import com.ebstudy.board.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GalleryPostMapper {
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
     * 썸네일 id를 게시글 테이블에 등록
     */
    void addThumbnailId(@Param("postId") Long postId, @Param("thumbnailId") Long thumbnailId);

    /**
     * 해당 postId 게시글을 삭제 처리(flag 설정)
     */
    void deletePost(PostDTO post);
}
