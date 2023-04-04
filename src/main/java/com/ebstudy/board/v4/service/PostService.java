package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.dto.SearchDTO;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.global.exception.CustomErrorCode;
import com.ebstudy.board.v4.repository.BoardMapper;
import com.ebstudy.board.v4.global.util.PostServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class PostService {

    private final BoardMapper boardMapper;
    private final PostServiceUtil postServiceUtil;

    // TODO: exception이 발생하게 될 때 반드시 핸들링해야 하는 경우만 처리하고 이외에는 알아서 throw되도록 처리한다.
    /**
     * 요구 데이터 포맷에 맞게 변환된 게시글 리스트를 가져오는 메소드
     * @param pagingValue 검색조건과 페이징 값
     * @return 게시글 리스트
     */
    public List<PostDTO> getPostList(PaginationDTO pagingValue) {
        return boardMapper.getPostList(pagingValue);
    }

    /**
     * Pagination에 사용되는 변수들을 반환하는 메소드
     * @param searchValues 검색 조건
     * @return 검색 조건을 포함한 페이징 값
     */
    public PaginationDTO getPaginationValues(SearchDTO searchValues) {

        // 검색 조건에 해당하는 게시글의 총 개수를 카운트
        int totalPostCount = boardMapper.getPostCount(searchValues);
        return postServiceUtil.calPagingValues(totalPostCount, searchValues);
    }

    /**
     * 게시글 불러오기 메소드
     * postId 파라미터에 대한 유효성 검증과 게시글 로딩을 수행
     * @param postId 게시글 id
     * @return 게시글 데이터와 viewName을 가진 ModelAndView 객체 반환
     */
    public PostDTO getPost(Long postId) {

        if (postId == null || postId <= 0) {
            log.info("파라미터 postId값이 유효하지 않음");
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        increaseHits(postId);

        return boardMapper.getPost(postId);
    }

    /**
     * 카테고리 리스트를 가져오는 메소드
     * @return 카테고리 리스트
     */
    public List<CategoryDTO> getCategoryList() {
        return boardMapper.getCategoryList();
    }

    /**
     * 비밀번호 암호화 및 생성시간을 추가해서 게시글을 저장하는 메소드
     * @param post 저장할 게시글
     */
    public void savePost(PostDTO post) {

        post.setPasswd(postServiceUtil.getSHA512(post.getPasswd()));
        post.setCreatedDate(String.valueOf(LocalDateTime.now()));

        boardMapper.savePost(post);
    }

    /**
     * 조회수 증가
     * @param postId 증가시킬 게시글의 id
     */
    public void increaseHits(Long postId) {
        boardMapper.increaseHits(postId);
    }

    /**
     * 게시글 수정
     * @param post 수정할 게시글 정보가 담긴 게시글 DTO
     */
    public void updatePost(PostDTO post) {

        PostDTO originPost = getPost(post.getPostId());
        // TODO: 2023/03/18 비밀번호 검증은 유효성 검증이 아니다. 별도의 service를 추가해야 함
        // 형식에 맞게 데이터 set 및 유효성 검증
        post.setPasswd(postServiceUtil.getSHA512(post.getPasswd()));
        post.setConfirmPasswd(originPost.getPasswd());
        post.setModifiedDate(String.valueOf(LocalDateTime.now()));

        boardMapper.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param post 삭제할 게시글 id 및 게시글 비밀번호
     */
    public void deletePost(PostDTO post) {

        PostDTO originPost = getPost(post.getPostId());

        // 형식에 맞게 데이터 set 및 유효성 검증
        post.setPasswd(postServiceUtil.getSHA512(post.getPasswd()));
        post.setConfirmPasswd(originPost.getPasswd());

        boardMapper.deletePost(post);
    }

}
