package com.ebstudy.board.inquiry.controller;

import com.ebstudy.board.dto.*;
import com.ebstudy.board.global.validator.CustomValidation;
import com.ebstudy.board.inquiry.service.InquiryCommentService;
import com.ebstudy.board.inquiry.service.InquiryFileService;
import com.ebstudy.board.inquiry.service.InquiryPostService;
import com.ebstudy.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class InquiryPostController {

    private final InquiryPostService postService;
    private final InquiryFileService fileService;
    private final UserService userService;
    private final InquiryCommentService commentService;

    /**
     * 게시글 리스트를 로딩
     *
     * @param searchValues 검색조건
     * @return 페이지 번호별로 로딩한 게시글 리스트
     */
    @GetMapping("/api/boards/inquiry")
    public ResponseEntity getPostList(@ModelAttribute SearchDTO searchValues) {

        // 받아온 검색조건을 입력해 pagingValues를 가져온다
        PaginationDTO pagingValues = postService.getPaginationValues(searchValues);
        // 받아온 페지네이션 값을 사용하여 게시글 리스트를 불러온다
        List<PostDTO> postList = postService.getPostList(pagingValues);

        HashMap<String, Object> postListResponse = new HashMap<>();
        postListResponse.put("pagingValues", pagingValues);
        postListResponse.put("postList", postList);

        return ResponseEntity.ok(postListResponse);
    }

    /**
     * 게시글 로딩
     *
     * @param postId 가져올 게시글 번호
     * @return 가져온 게시글 데이터
     */
    @GetMapping("/api/boards/inquiry/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {

        PostDTO post = postService.getPost(postId);
        List<FileDTO> fileList = fileService.getFileList(postId);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        HashMap<String, Object> postResponse = new HashMap<>();
        postResponse.put("post", post);
        postResponse.put("commentList", commentList);
        postResponse.put("fileList", fileList);

        return ResponseEntity.ok(postResponse);
    }

    /**
     * 게시글 작성 폼으로 이동
     *
     * @return 게시글 폼 데이터
     */
    @GetMapping("/api/boards/inquiry/new")
    public ResponseEntity getWriteForm() {

        UserDTO user = userService.getUserFromContext();

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);

        return ResponseEntity.ok(data);
    }

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글
     * @return HttpStatus를 가진 ResponseEntity<> 객체
     */
    @PostMapping("/api/boards/inquiry")
    public ResponseEntity savePost(@CustomValidation(value = {"title", "content", "secret"})
                                            @ModelAttribute PostDTO post) throws IOException {
        postService.savePost(post);
        fileService.saveFile(post.getPostId(), post.getFile());

        return ResponseEntity.ok(null);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @PutMapping("/api/boards/inquiry/{postId}")
    public ResponseEntity updatePost(@CustomValidation(value = {"title", "content", "secret"}) @ModelAttribute PostDTO post,
                                              @RequestPart(required = false) List<FileDTO> existingFiles) throws IOException {
        // Multipart/Form-Data 방식과 json타입의 객체를 같이 사용하려면 json파트에 대해 @RequestPart 어노테이션을 적용해 주면 된다.

        // 수정 요청한 게시글 데이터를 DB에서 가져와서 해당 정보 기준으로 비교한다(postId를 다르게 하고 authorId를 일치시키는 방식으로 위조할 수 있기 때문에)
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        // 게시글 먼저 수정
        postService.updatePost(post, originPost);
        fileService.updateFile(post.getPostId(), existingFiles, post.getFile());

        return ResponseEntity.ok(null);
    }

    /**
     * 게시글 삭제, flag 설정으로 숨김 처리
     *
     * @param post postId, passwd 값 전달
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @DeleteMapping("/api/boards/inquiry/{postId}")
    public ResponseEntity deletePost(@ModelAttribute PostDTO post) {

        // 수정 요청한 게시글의 작성자와 JWT안의 요청자 정보가 일치하는지 확인
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        postService.deletePost(post, originPost);

        return ResponseEntity.ok(null);
    }
}
