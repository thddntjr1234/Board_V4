package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter @ToString
public class PostDTO {

    // 논리 필드가 추가되게 된다면 그 부분에 대한 설명만 충분히 적으면 된다.
    private Long postId;
    private Long hits;
    private Long categoryId;
    private String createdDate;
    private String modifiedDate;
    private String title;
    private String content;
    private String author;
    private String category;
    private String passwd;
    private String secondPasswd; // 비밀번호 확인
    private boolean fileFlag = false; // checkExistence()로 파일 존재여부 확인 뒤 true로 변경
    private List<MultipartFile> file; // 게시글 저장 시 전달할 용도로 사용하는 파일 리스트
}
