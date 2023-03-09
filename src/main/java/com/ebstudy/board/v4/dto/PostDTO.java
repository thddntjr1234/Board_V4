package com.ebstudy.board.v4.dto;

import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class PostDTO {

    // 논리 필드가 추가되게 된다면 그 부분에 대한 설명만 충분히 적으면 된다.
    private Long postId; // 게시글 pk
    private Long hits; // 조회수
    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId; // 카테고리 테이블의 pk값(fk)
    private String createdDate; // 작성일
    private String modifiedDate; // 수정일
    @Size(min = 4, max = 99, message = "제목은 4글자 이상 100글자 미만이어야 합니다")
    private String title; // 제목
    @Size(min = 4, max = 1999, message = "내용은 4글자 이상 2000글자 미만이어야 합니다")
    private String content; // 내용
    @Size(min = 3, max = 4, message = "작성자명은 3글자 이상 5글자 미만이어야 합니다")
    private String author; // 작성자
    private String category; // 카테고리 이름

    @Pattern(regexp = "^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
            message = "비밀번호는 4글자 이상 16글자 미만, 영문과 숫자, 특수문자를 포함해야 합니다")
    private String passwd; // 비밀번호
    @Pattern(regexp = "^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
            message = "비밀번호는 4글자 이상 16글자 미만, 영문과 숫자, 특수문자를 포함해야 합니다")
    private String confirmPasswd; // 비밀번호 확인
    private boolean fileFlag = false; // checkExistence()로 파일 존재여부 확인 뒤 true로 변경
    private List<MultipartFile> file; // 게시글 저장 시 전달할 용도로 사용하는 파일 리스트

}
