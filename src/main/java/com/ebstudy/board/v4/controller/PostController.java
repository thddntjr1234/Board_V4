package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.dto.response.PostListResponseDTO;
import com.ebstudy.board.v4.dto.response.PostResponseDTO;
import com.ebstudy.board.v4.service.CommentService;
import com.ebstudy.board.v4.service.FileService;
import com.ebstudy.board.v4.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
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
    @GetMapping(value = {"/list/{pageNumber}", "/list"})
    public ResponseEntity<PostListResponseDTO> getPostList(@PathVariable(required = false) Integer pageNumber) {

        List<CategoryDTO> categoryList = postService.getCategoryList();
        PaginationDTO pagingValues = postService.getPaginationValues(pageNumber);
        List<PostDTO> postList = postService.getPostList(pagingValues.getStartPostNumber());

        // 반환하는 값에 어떤 정보가 들었는지 명확하게 알 수 있도록 Map 사용을 지양하기 위해 별도의 DTO를 추가
        PostListResponseDTO postListResponseDTO = PostListResponseDTO.builder()
                .categoryList(categoryList)
                .pagingValues(pagingValues)
                .postList(postList)
                .build();

        return new ResponseEntity<>(postListResponseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 로딩
     *
     * @param postId 가져올 게시글 번호
     * @return 가져온 게시글 데이터
     */
    @GetMapping(value = {"/view/{postId}", "/view"})
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable(required = false) Long postId) {

        // null, 0, ""은 list 페이지로 리다이렉트
        // TODO : 예외처리(ExceptionHandler
        if (postId == null || postId <= 0) {
            // redirect의 주체는 규약에 따라 각기 다름, 서버가 주체면 이 방식이 맞고 클라이언트가 주체면 true, false와 같은 규약된 정보만 리턴
//            mv.setViewName("redirect:/boards/free/list");
            log.info("파라미터 postId값이 유효하지 않음");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PostDTO post = postService.getPost(postId);
        List<FileDTO> fileList = fileService.getFileList(postId);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        // 반환하는 값에 어떤 정보가 들었는지 명확하게 알 수 있도록 Map 사용을 지양하기 위해 별도의 DTO를 추가
        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .post(post)
                .commentList(commentList)
                .fileList(fileList)
                .build();

        log.info("getPost 정상 수행에 따른 파일 리스트 및 댓글 리스트 로딩 완료");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * 게시글 작성 폼으로 이동
     *
     * @return 게시글 폼 viewName과 카테고리 리스트를 가진 ModelAndView 객체
     */
    @GetMapping("/write")
    public ResponseEntity<List<CategoryDTO>> getWriteForm() {

        List<CategoryDTO> categoryList = postService.getCategoryList();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    /**
     * 게시글 저장
     *
     * @param post 저장할 게시글
     * @return HttpStatus를 가진 ResponseEntity<> 객체
     */
    @PostMapping("/write")
    // ResponseEntity 로 리턴하면 raw type 경고가 나타나므로 와일드카드 ?를 선언해서 raw type의 불안정성을 제거
    public ResponseEntity<?> savePost(@ModelAttribute PostDTO post) {

        log.info("post: " + post);
        try {
            // TODO: @Transactional 어노테이션의 위치(컨트롤러? 혹은 각 서비스?)
            postService.savePost(post);
            log.info("savePost 수행 완료");
            fileService.saveFile(post.getPostId(), post.getFile()); // -> 이렇게 각 서비스를 실행하지 않고 원자성을 가지는 서비스 단위로 묶어서 실행해야 한다
        } catch (Exception e) {
            // TODO: 2/25 checked exception unchecked exception의 차이
            log.info("savePost 오류 발생, error type=" + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return
     */
    @PutMapping("/post/{postId}")
    public ResponseEntity<?> updatePost(@ModelAttribute PostDTO post) {

        try {
            postService.updatePost(post);
            //TODO: file update 메소드 추가
        } catch (Exception e) {
            log.info("updatePost 오류 발생, error:" + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 게시글 삭제, cascade로 db에서 자체 삭제하므로 부모인 게시글만 삭제하면 된다.
     * @param postId 삭제할 게시글 id
     * @return
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable(required = false) Long postId) {

        try {
            postService.deletePost(postId);
        } catch (Exception e) {
            log.info("deletePost 오류 발생, error" + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
