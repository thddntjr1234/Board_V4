package com.ebstudy.board.community.controller;

import com.ebstudy.board.dto.*;
import com.ebstudy.board.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.global.validator.CustomValidation;
import com.ebstudy.board.community.service.CommunityCommentService;
import com.ebstudy.board.community.service.CommunityFileService;
import com.ebstudy.board.community.service.CommunityPostService;
import com.ebstudy.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommunityPostController {

    private final CommunityPostService postService;
    private final UserService userService;
    private final CommunityFileService fileService;
    private final CommunityCommentService commentService;

    /**
     * 게시글 리스트를 로딩
     *
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
     *
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
     *
     * @return 게시글 폼 데이터
     */
    @GetMapping("/api/boards/free/new")
    public CommonApiResponseDTO<?> getWriteForm() {

        UserDTO user = userService.getUserFromContext();
        List<CategoryDTO> categoryList = postService.getCategoryList();

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("categoryList", categoryList);

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(data)
                .build();
    }

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글
     * @return HttpStatus를 가진 ResponseEntity<> 객체
     */
    @PostMapping("/api/boards/free")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public CommonApiResponseDTO<?> savePost(@CustomValidation(value = {"categoryId", "title", "content"})
                                            @ModelAttribute PostDTO post) throws IOException {
        postService.savePost(post);
        log.info("savePost 수행 완료");
        fileService.saveFile(post.getPostId(), post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @PutMapping("/api/boards/free/{postId}")
    public CommonApiResponseDTO<?> updatePost(@CustomValidation(value = {"title", "content"}) @ModelAttribute PostDTO post,
                                              @RequestPart(required = false) List<FileDTO> existingFiles) throws IOException {
        // Multipart/Form-Data 방식과 json타입의 객체를 같이 사용하려면 json파트에 대해 @RequestPart 어노테이션을 적용해 주면 된다.

        // 수정 요청한 게시글 데이터를 DB에서 가져와서 해당 정보 기준으로 비교한다(postId를 다르게 하고 authorId를 일치시키는 방식으로 위조할 수 있기 때문에)
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        // 게시글 먼저 수정
        postService.updatePost(post);
        fileService.updateFile(post.getPostId(), existingFiles, post.getFile());

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 게시글 삭제, flag 설정으로 숨김 처리
     *
     * @param post postId, passwd 값 전달
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @DeleteMapping("/api/boards/free/{postId}")
    public CommonApiResponseDTO<?> deletePost(@ModelAttribute PostDTO post) {

        // 수정 요청한 게시글의 작성자와 JWT안의 요청자 정보가 일치하는지 확인
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        postService.deletePost(post);

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }
}
