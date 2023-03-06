package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter @ToString
public class PostDTO {

    // 논리 필드가 추가되게 된다면 그 부분에 대한 설명만 충분히 적으면 된다.
    private Long postId; // 게시글 pk
    private Long hits; // 조회수
    private Long categoryId; // 카테고리 테이블의 pk값(fk)
    private String createdDate; // 작성일
    private String modifiedDate; // 수정일
    private String title; // 제목
    private String content; // 내용
    private String author; // 작성자
    private String category; // 카테고리 이름
    private String passwd; // 비밀번호
    private String secondPasswd; // 비밀번호 확인
    private boolean fileFlag = false; // checkExistence()로 파일 존재여부 확인 뒤 true로 변경
    private List<MultipartFile> file; // 게시글 저장 시 전달할 용도로 사용하는 파일 리스트

}
