package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.v4.global.validator.CustomValidation;
import com.ebstudy.board.v4.service.CommentService;
import com.ebstudy.board.v4.service.FileService;
import com.ebstudy.board.v4.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
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
     * @param searchValues 검색조건
     * @return 페이지 번호별로 로딩한 게시글 리스트
     */
    @GetMapping("/api/boards/free")
    public CommonApiResponseDTO<?> getPostList(@ModelAttribute SearchDTO searchValues) {

        List<CategoryDTO> categoryList = postService.getCategoryList();

        // 받아온 검색조건을 입력해 pagingValues를 가져온다
        PaginationDTO pagingValues = postService.getPaginationValues(searchValues);
        // 받아온 페지네이션 값을 사용하여 게시글 리스트를 불러온다
        List<PostDTO> postList = postService.getPostList(pagingValues);

        HashMap<String, Object> postListResponse = new HashMap<>();
        postListResponse.put("categoryList", categoryList);
        postListResponse.put("pagingValues", pagingValues);
        postListResponse.put("postList", postList);

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(postListResponse)
                .build();
    }

    /**
     * 게시글 로딩
     * /boards/free/3
     * @param postId 가져올 게시글 번호
     * @return 가져온 게시글 데이터
     */
    @GetMapping("/api/boards/free/{postId}")
    public CommonApiResponseDTO<?> getPost(@PathVariable Long postId) {

        PostDTO post = postService.getPost(postId);
        List<FileDTO> fileList = fileService.getFileList(postId);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        log.info("getPost 정상 수행에 따른 게시글 로드 완료");

        HashMap<String, Object> postResponse = new HashMap<>();
        postResponse.put("post", post);
        postResponse.put("commentList", commentList);
        postResponse.put("fileList", fileList);

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(postResponse)
                .build();
    }

    /**
     * 게시글 작성 폼으로 이동
     * /boards/free/new
     * @return 게시글 폼 viewName과 카테고리 리스트를 가진 ModelAndView 객체
     */
    @GetMapping("/api/boards/free/new")
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
    @PostMapping("/api/boards/free")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public CommonApiResponseDTO<?> savePost(@CustomValidation(value = {"categoryId", "title", "content", "author", "passwd", "confirmPasswd"})
                                                @ModelAttribute PostDTO post) throws IOException {

        postService.savePost(post);
        log.info("savePost 수행 완료");
        fileService.saveFile(post.getPostId(), post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    // TODO: 3/11 리뷰에서 받았던 유효성 검증은 가급적 컨트롤러에서 하기를 어떻게 지킬 수 있을까?
    //  Update, Delete는 post에서 패스워드를 애초에 받아서 가지고 있게 하는 방식으로 하면 된다고 쳐도 수정, 삭제 버튼시 한번 체크하고 넘어가는 부분에서는 어쩔 수 없이 DAO에서 체크할 수 밖에 없다.
    // -> 위와 같은 부분에서는 애초에 패스워드 비교가 유효성 검증이 아니라 하나의 서비스 로직인 셈이다. 당연히 서비스 로직에서 처리해야 하는 것으로
    //  맞지 맞는 옷에 억지로 몸을 낑겨넣는 것과 같다.
    /**
     * 게시글 수정
     * /boards/free/3 PUT
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @PutMapping("/api/boards/free/{postId}")
    public CommonApiResponseDTO<?> updatePost(@CustomValidation(value = {"title", "content", "author"}) @ModelAttribute  PostDTO post,
                                              @RequestPart(required = false) List<FileDTO> existingFiles) throws IOException {
        // Multipart/Form-Data 방식과 json타입의 객체를 같이 사용하려면 json파트에 대해 @RequestPart 어노테이션을 적용해 주면 된다.

        // 게시글 먼저 수정
        postService.updatePost(post);
        fileService.updateFile(post.getPostId(), existingFiles ,post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 게시글 삭제, flag 설정으로 숨김 처리
     * /boards/free/3 DELTE
     * @param post postId, passwd 값 전달
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @DeleteMapping("/api/boards/free")
    public CommonApiResponseDTO<?> deletePost(@ModelAttribute PostDTO post) {

        postService.deletePost(post);
        
        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 테스트용
     */
    @GetMapping("/post/test")
    public void errorThrower(@CustomValidation(value = {"passwd", "confirmPasswd"}) PostDTO postDTO) throws RuntimeException {
        int[] test = new int[5];

    }
}
