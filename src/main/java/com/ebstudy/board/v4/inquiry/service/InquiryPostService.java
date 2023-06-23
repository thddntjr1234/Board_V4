package com.ebstudy.board.v4.inquiry.service;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.global.authority.Role;
import com.ebstudy.board.v4.global.exception.CustomErrorCode;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.global.util.PostServiceUtil;
import com.ebstudy.board.v4.inquiry.repository.InquiryPostMapper;
import com.ebstudy.board.v4.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class InquiryPostService {

    private final InquiryPostMapper boardMapper;
    private final PostServiceUtil postServiceUtil;
    private final UserService userService;

    /**
     * 비밀 게시글 처리된 게시글 리스트를 가져오는 메소드
     *
     * @param pagingValue 검색조건과 페이징 값
     * @return 비밀 게시글 처리된 게시글 리스트
     */
    public List<PostDTO> getPostList(PaginationDTO pagingValue) {

        // '내 게시글' 필터 처리를 위한 userId 값 입력
        // Security Context 정보를 조회한 값을 사용한다.
        Long userId = null;
        if (pagingValue.getFilter() != null) {
            // TODO: 2023/06/01 Optional orelseThrow 처리를 getMyUserWithAuthorites에서 처리하고 일반 UserDTO 객체로 돌려주는게 맞는 것 같다.
            userId = userService.getUserFromContext().getUserId();
        }

        List<PostDTO> postList = boardMapper.getPostList(pagingValue, userId);

        // 비밀 게시글 처리가 된 경우 게시글 리스트 반환 시 데이터를 노출하지 않아야 한다.
        // 필요한 데이터만 추가된 게시글로 변환
        return postList.stream().map(post -> {
            if (post.getSecret()) {
                return PostDTO.builder()
                        .postId(post.getPostId())
                        .category(post.getCategory())
                        .createdDate(post.getCreatedDate())
                        .modifiedDate(post.getModifiedDate())
                        .title(post.getTitle())
                        .author(post.getAuthor())
                        .hits(post.getHits())
                        .answerStatus(post.getAnswerStatus())
                        .secret(post.getSecret())
                        .build();
            } else {
                return post;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Pagination에 사용되는 변수들을 반환하는 메소드
     *
     * @param searchValues 검색 조건
     * @return 검색 조건을 포함한 페이징 값
     */
    public PaginationDTO getPaginationValues(SearchDTO searchValues) {

        // Pagination 값을 처리하기 위해 getPostCount 시 내 게시글만 조회할 수 있도록 해당 메소드에서도 유저 정보를 가져와야 한다.
        Long userId = null;
        if (searchValues.getFilter() != null) {
            userId = userService.getUserFromContext().getUserId();
        }

        // 입력된 검색 조건을 전처리한다
        searchValues = postServiceUtil.preProcessSearchValues(searchValues);

        // 검색 조건에 해당하는 게시글의 총 개수를 카운트
        int totalPostCount = boardMapper.getPostCount(searchValues, userId);

        return postServiceUtil.calPagingValues(totalPostCount, searchValues);
    }

    /**
     * 게시글 불러오기 메소드
     * postId 파라미터에 대한 유효성 검증과 비밀 게시글 열람권한 검증을 수행하고 게시글을 반환한다.
     *
     * @param postId 게시글 id
     * @return 게시글 데이터
     */
    public PostDTO getPost(Long postId) {

        if (postId == null || postId <= 0) {
            log.error("파라미터 postId값이 유효하지 않음");
            throw new CustomException(CustomErrorCode.INVALID_REQUEST);
        }

        PostDTO post = boardMapper.getPost(postId);

        if (post == null) {
            log.error("존재하지 않는 게시글을 요청");
            throw new CustomException(CustomErrorCode.INVALID_POST_REQUEST);
        }

        // 비밀 게시글 작성자 인증
        if (post.getSecret()) {
            UserDTO userInfo = userService.getUserFromContext();

            // 게시글 작성자와 api 요청자가 동일한지 확인한다.
            if (!userInfo.getRole().equals(Role.ROLE_ADMIN) && !userInfo.getUserId().equals(post.getAuthorId())) {
                throw new CustomException(CustomErrorCode.DENIED_POST_REQUEST);
            }
        }

        increaseHits(postId);
        return post;
    }

    /**
     * 비밀번호 암호화 및 생성시간을 추가해서 게시글을 저장하는 메소드
     *
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
     *
     * @param postId 증가시킬 게시글의 id
     */
    public void increaseHits(Long postId) {
        boardMapper.increaseHits(postId);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 게시글 정보가 담긴 게시글 DTO
     */
    public void updatePost(PostDTO post, PostDTO originPost) {
        // 답변된 게시글이면 수정 거부 예외를 throw
        if (checkPostAnswerStatus(originPost)) {
            throw new CustomException(CustomErrorCode.DENIED_POST_UPDATE);
        }

        post.setModifiedDate(String.valueOf(LocalDateTime.now()));
        boardMapper.updatePost(post);
    }

    /**
     * 게시글 삭제
     *
     * @param post 삭제할 게시글 id 및 게시글 비밀번호
     */
    public void deletePost(PostDTO post, PostDTO originPost) {
        // 답변된 게시글이면 삭제 거부 예외를 throw
        if (checkPostAnswerStatus(originPost)) {
            throw new CustomException(CustomErrorCode.DENIED_POST_DELETE);
        }

        boardMapper.deletePost(post);
    }

    /**
     * 해당 게시글이 이미 답변된 게시글인지 확인한다.
     *
     * @param post 답변 여부를 확인할 게시글
     * @return Boolean
     */
    public boolean checkPostAnswerStatus(PostDTO post) {
        return post.getAnswerStatus();
    }
}
