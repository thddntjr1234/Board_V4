package com.ebstudy.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    // 게시글 pk
    private Long postId;

    // 조회수
    private Long hits;

    // 카테고리 테이블의 pk값(fk)
    private Long categoryId;

    // 작성일
    private String createdDate;

    // 수정일
    private String modifiedDate;

    // 제목
    private String title;

    // 내용
    private String content;

    // 작성자 ID
    private Long authorId;

    // 작성자명
    private String author;

    // 카테고리 이름
    private String category;

    // 채택된 댓글 ID
    private Long adoptedCommentId;

    // 문의 답변 여부
    private Boolean answerStatus;

    // 비공개 여부
    private Boolean secret;

    // 댓글 개수
    private Long commentCount;

    // 공지사항 노출 게시판 지정
    private String target;

    // file 존재 여부, checkExistence()로 파일 존재여부 확인 뒤 true로 변경
    private Boolean fileFlag;

    // 게시글 저장 시 전달할 용도로 사용하는 파일 리스트
    private List<MultipartFile> file;

    // 이미지 썸네일 ID
    private Long thumbnailId;

    // 이미지 썸네일 접근 주소
    private String thumbnailPath;
}
