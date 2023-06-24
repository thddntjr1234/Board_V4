package com.ebstudy.board.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

    // 소속된 게시글의 pk값(fk)
    private Long postId;

    // 파일의 pk값
    private Long fileId;

    // UUID가 추가된 서버상의 파일 이름
    private String fileName;

    // 사용자가 추가한 파일의 실제 이름
    private String fileRealName;

    // 확장자
    private String extension;

    // 크기
    private Long size;

    // 이미지에 접근할 수 있는 url
    private String imgAccessUrl;
}
