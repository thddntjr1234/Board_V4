package com.ebstudy.board.gallery.controller;

import com.ebstudy.board.dto.*;
import com.ebstudy.board.dto.response.CommonApiResponseDTO;
import com.ebstudy.board.global.validator.CustomValidation;
import com.ebstudy.board.gallery.service.GalleryCommentService;
import com.ebstudy.board.gallery.service.GalleryFileService;
import com.ebstudy.board.gallery.service.GalleryPostService;
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
public class GalleryPostController {

    private final GalleryPostService postService;
    private final UserService userService;
    private final GalleryFileService fileService;
    private final GalleryCommentService commentService;

    /**
     * 게시글 리스트를 로딩
     *
     * @param searchValues 검색조건
     * @return 페이지 번호별로 로딩한 게시글 리스트
     */
    @GetMapping("/api/boards/gallery")
    public CommonApiResponseDTO<?> getPostList(@ModelAttribute SearchDTO searchValues) {

        // 받아온 검색조건을 입력해 pagingValues를 가져온다
        PaginationDTO pagingValues = postService.getPaginationValues(searchValues);
        // 받아온 페지네이션 값을 사용하여 게시글 리스트를 불러온다
        List<PostDTO> postList = postService.getPostList(pagingValues);

        HashMap<String, Object> postListResponse = new HashMap<>();
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
    @GetMapping("/api/boards/gallery/{postId}")
    public CommonApiResponseDTO<?> getPost(@PathVariable Long postId) {

        PostDTO post = postService.getPost(postId);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        log.info("getPost 정상 수행에 따른 게시글 로드 완료");

        HashMap<String, Object> postResponse = new HashMap<>();
        postResponse.put("post", post);
        postResponse.put("commentList", commentList);

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
    @GetMapping("/api/boards/gallery/new")
    public CommonApiResponseDTO<?> getWriteForm() {

        UserDTO user = userService.getUserFromContext();

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);

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
    @PostMapping("/api/boards/gallery")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public CommonApiResponseDTO<?> savePost(@CustomValidation(value = {"title", "content"}) @ModelAttribute PostDTO post,
                                            @RequestPart(required = false) List<FileDTO> registeredFileList) throws IOException {

        postService.savePost(post);

        // 게시글 저장 후 사전에 등록된 이미지 파일 중 실제 등록된 파일에 대해 처리
        fileService.updateRegisteredFilesWithPostId(post.getPostId(), registeredFileList);
        fileService.moveTempFileToLocalStorage(registeredFileList);

        if (!registeredFileList.isEmpty()) {
            // 썸네일 생성 및 생성된 썸네일 PK값 반환
            Long thumbnailId = fileService.saveThumbnailImage(post.getPostId(), registeredFileList);
            // 게시글에 썸네일 ID 추가
            postService.addThumbnailId(post.getPostId(), thumbnailId);
        }

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
    @PutMapping("/api/boards/gallery/{postId}")
    public CommonApiResponseDTO<?> updatePost(@CustomValidation(value = {"title", "content"}) @ModelAttribute PostDTO post,
                                              @RequestPart(required = false) List<FileDTO> registeredFileList) throws IOException {
        // Multipart/Form-Data 방식과 json타입의 객체를 같이 사용하려면 json파트에 대해 @RequestPart 어노테이션을 적용해 주면 된다.

        // 수정 요청한 게시글 데이터를 DB에서 가져와서 해당 정보 기준으로 비교한다(postId를 다르게 하고 authorId를 일치시키는 방식으로 위조할 수 있기 때문에)
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        // 게시글 먼저 수정
        postService.updatePost(post);
        List<FileDTO> originFileList = fileService.getFileList(post.getPostId());

        fileService.updateFiles(post.getPostId(), registeredFileList, originFileList);

        // 썸네일 수정이 필요한 경우 썸네일 수정 작업을 수행
        if (!registeredFileList.isEmpty()) {

            Long beforeThumbnailId = originPost.getThumbnailId();
            // 썸네일 생성 및 생성된 썸네일 PK값 반환
            Long thumbnailId = fileService.saveThumbnailImage(post.getPostId(), registeredFileList);

            // 게시글에 썸네일 ID 추가
            postService.addThumbnailId(post.getPostId(), thumbnailId);
            // 새로운 썸네일 추가 이후 기존 썸네일을 삭제 처리(로컬에서의 용량이 2KB정도이므로 로컬에서 삭제하지는 않고 데이터 일치를 위해 DB에서만 삭제한다)
            fileService.deleteThumbnail(beforeThumbnailId);
        }

        return CommonApiResponseDTO.builder()
                .success(true)
                .build();
    }

    /**
     * 게시글 수정 화면에서 필요한 게시글 정보를 가져온다.
     *
     * @param postId 수정할 게시글 id
     * @return 게시글 정보를 가진 CommonResponseApi 객체
     */
    @GetMapping("/api/boards/gallery/{postId}/edit")
    public CommonApiResponseDTO<?> getPostWithFileList(@PathVariable Long postId) {

        PostDTO post = postService.getPost(postId);
        List<FileDTO> fileList = fileService.getFileList(postId);

        log.info("getPost 정상 수행에 따른 게시글 로드 완료");

        HashMap<String, Object> postResponse = new HashMap<>();
        postResponse.put("post", post);
        postResponse.put("fileList", fileList);

        return CommonApiResponseDTO.builder()
                .success(true)
                .data(postResponse)
                .build();
    }

    /**
     * 게시글 삭제, flag 설정으로 숨김 처리
     *
     * @param post postId, passwd 값 전달
     * @return 공통 반환타입 CommonApiResponseDTO 객체
     */
    @DeleteMapping("/api/boards/gallery/{postId}")
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
