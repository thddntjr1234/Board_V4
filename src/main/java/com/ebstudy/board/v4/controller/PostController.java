package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.dto.response.PostListResponseDTO;
import com.ebstudy.board.v4.dto.response.PostResponseDTO;
import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import com.ebstudy.board.v4.service.CommentService;
import com.ebstudy.board.v4.service.FileService;
import com.ebstudy.board.v4.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated // TODO: 3/11(질문) Custom Validation을 사용할 때 @Validated를 적용해야 Custom Validation이 적용되었음.
@RequestMapping(value = "/boards/free")
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final CommentService commentService;

    // TODO: 전역 에러 핸들러 쓸 필요가 있다.(exception을 최종적으로 서버에서 뱉을 때 그 에러를 그대로 페이지에 표시하지 않고 이를 캐치해서 처리해서 뱉을 수 있도록 하는거)

    /**
     * 게시글 리스트를 로딩
     *
     * @param pageNumber 로딩할 페이지 번호
     * @return 페이지 번호별로 로딩한 게시글 리스트
     */
    @GetMapping(value = {"/list/{pageNumber}", "/list"}) //TODO: 3/4. 검색 조건이 추가되게 된다면 path로 뺄 것과 파라미터로 뺄 것에 대해 고민하는게 좋다.
    public CommonApiResponseDTO<?> getPostList(@PathVariable(required = false) Integer pageNumber) {

        List<CategoryDTO> categoryList = postService.getCategoryList();
        PaginationDTO pagingValues = postService.getPaginationValues(pageNumber);
        List<PostDTO> postList = postService.getPostList(pagingValues.getStartPostNumber());

        log.info("getPostList 정상 수행에 따른 게시글 리스트 로드 완료");

        PostListResponseDTO postListResponseDTO = PostListResponseDTO.builder()
                .categoryList(categoryList)
                .pagingValues(pagingValues)
                .postList(postList)
                .build();

        return CommonApiResponseDTO.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .data(postListResponseDTO)
                .build();
    }

    /**
     * 게시글 로딩
     *
     * @param postId 가져올 게시글 번호
     * @return 가져온 게시글 데이터
     */
    @GetMapping("/post/{postId}")
    public CommonApiResponseDTO<?> getPost(@PathVariable(required = false) Long postId) {

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
     *
     * @return 게시글 폼 viewName과 카테고리 리스트를 가진 ModelAndView 객체
     */
    @GetMapping("/post/form")
    public CommonApiResponseDTO<?> getWriteForm() {

        List<CategoryDTO> categoryList = postService.getCategoryList();

        return CommonApiResponseDTO.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .data(categoryList)
                .build();
    }

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글
     * @return HttpStatus를 가진 ResponseEntity<> 객체
     */
    @PostMapping("/post")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public CommonApiResponseDTO<?> savePost(@ModelAttribute @Valid @EqualEachPasswd({"passwd", "confirmPasswd"}) PostDTO post) throws IOException {

        log.info("post: " + post);
        postService.savePost(post);
        log.info("savePost 수행 완료");
        fileService.saveFile(post.getPostId(), post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return
     */
    @PutMapping("/post/{postId}")
    public CommonApiResponseDTO<?> updatePost(@ModelAttribute PostDTO post) {

        postService.updatePost(post);
        //TODO: file update 메소드 추가

        return CommonApiResponseDTO.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * 게시글 삭제, cascade로 db에서 자체 삭제하므로 부모인 게시글만 삭제하면 된다.
     *
     * @param postId 삭제할 게시글 id
     * @return
     */
    @DeleteMapping("/post/{postId}")
    public CommonApiResponseDTO<?> deletePost(@Validated @EqualEachPasswd({"passwd", "confirmPasswd"}) @PathVariable(required = false) Long postId) {

        postService.deletePost(postId);

        return CommonApiResponseDTO.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .build();
    }
}
