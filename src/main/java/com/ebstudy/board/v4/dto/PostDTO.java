package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
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

    private String author;

    // 카테고리 이름
    private String category;

    // 비밀번호
    private String passwd;

    // 비밀번호 확인
    private String confirmPasswd;

    // file 존재 여부, checkExistence()로 파일 존재여부 확인 뒤 true로 변경
    private Boolean fileFlag;

    // 게시글 저장 시 전달할 용도로 사용하는 파일 리스트
    private List<MultipartFile> file;

}
