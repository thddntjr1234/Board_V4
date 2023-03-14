package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.CategoryDTO;
import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.global.exception.ErrorCode;
import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import com.ebstudy.board.v4.repository.BoardMapper;
import com.ebstudy.board.v4.global.util.PostServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class PostService {

    private final BoardMapper boardMapper;
    private final PostServiceUtil postServiceUtil;

    //TODO: 각 메소드별로 unchecked exception 발생할 수 있는지 여부 찾아서 customexception 던지기
    /**
     * 요구 데이터 포맷에 맞게 변환된 게시글 리스트를 가져오는 메소드
     * @param pagingValue 검색조건과 페이징 값 PaginationDTO 객체
     * @return 게시글 리스트
     */
    public List<PostDTO> getPostList(PaginationDTO pagingValue) {

        List<PostDTO> postList = new LinkedList<>();
        //TODO: Pagination Values 계산
        List<PostDTO> sourcePostList = boardMapper.getPostList(pagingValue);

        for (PostDTO post : sourcePostList) {
            PostDTO convertedPost = postServiceUtil.convertToListFormat(post);
            postList.add(convertedPost);
        }

        return postList;
    }

    /**
     * Pagination에 사용되는 변수들을 반환하는 메소드
     * @param pageNumber uri로 입력된 요청 페이지 값
     * @return 페이징 값 및 SELECT LIMIT
     */
    public PaginationDTO getPaginationValues(Integer pageNumber) {

        int totalPostCount = boardMapper.getPostCount();
        return postServiceUtil.calPagingValues(totalPostCount, pageNumber);
    }

    /**
     * 게시글 불러오기 메소드
     * postId 파라미터에 대한 유효성 검증과 게시글 로딩을 수행
     * @param postId 게시글 id
     * @return 게시글 데이터와 viewName을 가진 ModelAndView 객체 반환
     */
    public PostDTO getPost(Long postId) {
        // TODO: custom exception
//        if (postId == null || postId <= 0) {
//            log.info("파라미터 postId값이 유효하지 않음");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        increaseHits(postId);

        PostDTO post = postServiceUtil.convertPostDataFormat(boardMapper.getPost(postId));
        return post;
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
     * @param requestPost 수정할 게시글 정보가 담긴 게시글 DTO
     */
    public void updatePost(PostDTO requestPost) {

        PostDTO originPost = getPost(requestPost.getPostId());

        // 형식에 맞게 데이터 set 및 유효성 검증
        requestPost.setPasswd(postServiceUtil.getSHA512(requestPost.getPasswd()));
        requestPost.setConfirmPasswd(originPost.getPasswd());
        requestPost.setModifiedDate(String.valueOf(LocalDateTime.now()));

        // TODO: update 시에 기존 db에서 originPasswd를 가져오는 과정 때문에 @EqualEachPasswd 어노테이션을 쓸 수 있는 위치가 되지 않는다.
        //  결국 별도 util 안에 같은 기능을 하는 메소드를 만들어야 했는데, 이렇게 파라미터에 붙여서 Custom Validator를 적용할 수 없다면 어떻게 해야 할까?
        //  -> boardMapper Interface에서 db에 넣기 전 유효성 검증을 하는 방식(도메인 계층에 해당)으로 처리할 수 있겠다. 각 계층마다 사용할 수 있다는 걸 깜빡했다.
//        postServiceUtil.isSamePasswd(requestPost.getPasswd(), originPost.getPasswd()); Mapper 메소드에 선언

        boardMapper.updatePost(requestPost);
    }

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 id
     */
    public void deletePost(Long postId) {
        boardMapper.deletePost(postId);
    }
}
