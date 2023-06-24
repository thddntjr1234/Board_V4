package com.ebstudy.board.dto;

import com.ebstudy.board.global.authority.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    // 회원 PK값
    private Long userId;
    // 회원 아이디
    private String loginId;
    // 회원 이름
    private String name;
    // 회원 이메일
    private String email;
    // 회원 비밀번호
    private String passwd;
    // 계정 활성화 여부
    private Boolean activated;
    // 회원 권한
    private Role role;
}
