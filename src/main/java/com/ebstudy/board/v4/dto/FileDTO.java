package com.ebstudy.board.v4.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {
    private Long postId; // 소속된 게시글의 pk값(fk)
    private Long fileId; // 파일의 pk값
    private String fileName; // UUID가 추가된 서버상의 파일 이름
    private String fileRealName; // 사용자가 추가한 파일의 실제 이름
    private String extension; // 확장자
    private Long size; // 크기
}
