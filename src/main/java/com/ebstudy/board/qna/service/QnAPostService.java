package com.ebstudy.board.qna.service;

import com.ebstudy.board.dto.*;
import com.ebstudy.board.global.exception.CustomErrorCode;
import com.ebstudy.board.global.exception.CustomException;
import com.ebstudy.board.global.util.PostServiceUtil;
import com.ebstudy.board.qna.repository.QnAPostMapper;
import com.ebstudy.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class QnAPostService {

    private final QnAPostMapper boardMapper;
    private final PostServiceUtil postServiceUtil;
    private final UserService userService;

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

        // 입력된 검색어를 전처리한다.
        searchValues = postServiceUtil.preProcessSearchValues(searchValues);

        // 검색 조건에 해당하는 게시글의 총 개수를 카운트
        int totalPostCount = boardMapper.getPostCount(searchValues);

        return postServiceUtil.calPagingValues(totalPostCount, searchValues);
    }

    /**
     * 게시글 불러오기 메소드
     * postId 파라미터에 대한 유효성 검증과 게시글 로딩을 수행
     * @param postId 게시글 id
     * @return 게시글 데이터
     */
    public PostDTO getPost(Long postId) {

        if (postId == null || postId <= 0) {
            log.info("파라미터 postId값이 유효하지 않음");
            throw new CustomException(CustomErrorCode.INVALID_POST_REQUEST);
        }

        Optional<PostDTO> post = Optional.ofNullable(boardMapper.getPost(postId));
        if (post.isEmpty()) {
            log.info("존재하지 않는 게시글을 요청");
            throw new CustomException(CustomErrorCode.INVALID_POST_REQUEST);
        }

        increaseHits(postId);

        return post.get();
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
        UserDTO user = userService.getUserFromContext();

        post.setAuthorId(user.getUserId());
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
        post.setModifiedDate(String.valueOf(LocalDateTime.now()));
        boardMapper.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param post 삭제할 게시글 id 및 게시글 비밀번호
     */
    public void deletePost(PostDTO post) {
        boardMapper.deletePost(post);
    }

    /**
     * 게시글 데이터 내 채택된 댓글의 id값을 데이터베이스에 추가
     * @param post 채택된 댓글의 id값이 있는 게시글 데이터
     */
    public void adoptComment(PostDTO post) {
        boardMapper.adoptComment(post);
    }
}
