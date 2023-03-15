package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.dto.response.PostListResponseDTO;
import com.ebstudy.board.v4.dto.response.PostResponseDTO;
import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import com.ebstudy.board.v4.service.CommentService;
import com.ebstudy.board.v4.service.FileService;
import com.ebstudy.board.v4.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final CommentService commentService;

    /**
     * 게시글 리스트를 로딩
     * /boards/free?keyword=ss&page=3&......
     *
     * @param pageNumber 로딩할 페이지 번호
     * @return 페이지 번호별로 로딩한 게시글 리스트
     */

    @GetMapping("/boards/free")
    public CommonApiResponseDTO<?> getPostList(Integer pageNumber, Integer categoryId, String keyword,
                                               String startDate, String endDate) {

        List<CategoryDTO> categoryList = postService.getCategoryList();
        PaginationDTO pagingValues = postService.getPaginationValues(pageNumber);

        // 일일히 파라미터를 선언해준 것은 PaginationDTO에 페이징을 위한 필드가 있기 때문에
        // 다른 역할을 하는 필드가 섞인 DTO에서 어느 값이 들어가는지 확실하게 알 수 있기 위함임
        pagingValues.setCategoryId(categoryId);
        pagingValues.setKeyword(keyword);
        pagingValues.setStartDate(startDate);
        pagingValues.setEndDate(endDate);

        List<PostDTO> postList = postService.getPostList(pagingValues);

        log.info("getPostList 정상 수행에 따른 게시글 리스트 로드 완료");

        PostListResponseDTO postListResponseDTO = PostListResponseDTO.builder()
                .categoryList(categoryList)
                .pagingValues(pagingValues)
                .postList(postList)
                .build();

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(postListResponseDTO)
                .build();
    }

    /**
     * 게시글 로딩
     * /boards/free/3
     * @param postId 가져올 게시글 번호
     * @return 가져온 게시글 데이터
     */
    @GetMapping("/boards/free/{postId}")
    public CommonApiResponseDTO<?> getPost(@PathVariable Long postId) {

        PostDTO post = postService.getPost(postId);
        List<FileDTO> fileList = fileService.getFileList(postId);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        log.info("getPost 정상 수행에 따른 게시글 로드 완료");

        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                .post(post)
                .commentList(commentList)
                .fileList(fileList)
                .build();

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(postResponseDTO)
                .build();
    }

    /**
     * 게시글 작성 폼으로 이동
     * /boards/free/new
     * @return 게시글 폼 viewName과 카테고리 리스트를 가진 ModelAndView 객체
     */
    @GetMapping("/boards/free/new")
    public CommonApiResponseDTO<?> getWriteForm() {

        List<CategoryDTO> categoryList = postService.getCategoryList();

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(categoryList)
                .build();
    }

    /**
     * 게시글 저장
     * /boards/free POST
     * @param post 저장할 게시글
     * @return HttpStatus를 가진 ResponseEntity<> 객체
     */
    @PostMapping("/boards/free")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public CommonApiResponseDTO<?> savePost(@ModelAttribute @Valid @EqualEachPasswd({"passwd", "confirmPasswd"}) PostDTO post) throws IOException {

        log.info("post: " + post);
        postService.savePost(post);
        log.info("savePost 수행 완료");
        fileService.saveFile(post.getPostId(), post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    // TODO: 3/11 리뷰에서 받았던 유효성 검증은 가급적 컨트롤러에서 하기를 어떻게 지킬 수 있을까?
    //  Update, Delete는 post에서 패스워드를 애초에 받아서 가지고 있게 하는 방식으로 하면 된다고 쳐도 수정, 삭제 버튼시 한번 체크하고 넘어가는 부분에서는 어쩔 수 없이 DAO에서 체크할 수 밖에 없다.

    /**
     * 게시글 수정
     * /boards/free/3 PUT
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @PutMapping("/boards/free/{postId}")
    public CommonApiResponseDTO<?> updatePost(@ModelAttribute @Valid PostDTO post) {

        postService.updatePost(post);
        //TODO: file update 메소드 추가

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 게시글 삭제, cascade로 db에서 자체 삭제하므로 부모인 게시글만 삭제하면 된다.
     * /boards/free/3 DELTE
     * @param postId 삭제할 게시글 id
     * @return
     */
    @DeleteMapping("/boards/free/{postId}")
    public CommonApiResponseDTO<?> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
