package com.ebstudy.board.user.service;


import com.ebstudy.board.dto.UserDTO;
import com.ebstudy.board.global.authority.Role;
import com.ebstudy.board.global.exception.CustomErrorCode;
import com.ebstudy.board.global.exception.CustomException;
import com.ebstudy.board.global.util.SecurityUtil;
import com.ebstudy.board.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    /**
     * 회원가입을 수행하는 메소드, 구현만 한 상태
     * @param userDTO 회원가입할 유저 정보
     * @return 가입 성공시 유저 정보를 반환
     * @throws DuplicateMemberException
     */
    @Transactional
    public UserDTO signup(UserDTO userDTO) throws DuplicateMemberException {
        if (userMapper.findUserByLoginId(userDTO.getLoginId()) == null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        UserDTO user = UserDTO.builder()
                .loginId(userDTO.getName())
                .passwd(passwordEncoder.encode(userDTO.getPasswd()))
                .name(userDTO.getName())
                .role(Role.ROLE_USER)
                .activated(true)
                .build();
        // mapper save
        // userMapper.save(user)

        // return boolean
        return user;
    }

    /**
     * 관리자가 유저 정보를 조회하기 위해 사용하는 메소드.
     * @param loginId 조회할 유저 로그인 아이디
     * @return 조회한 유저 정보
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthorities(String loginId) {

        UserDTO queryResult = userMapper.findUserByLoginId(loginId);
        UserDTO user = UserDTO
                .builder()
                .userId(queryResult.getUserId())
                .loginId(queryResult.getLoginId())
                .name(queryResult.getName())
                .email(queryResult.getEmail())
                .role(queryResult.getRole())
                .build();

        return Optional.ofNullable(user);
    }

    /**
     * 자신의 유저 정보를 조회하기 위해 사용하는 메소드
     * @return 자신의 유저 정보
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getMyUserWithAuthorities() {

        Optional<String> loginId = SecurityUtil.getCurrentUsername();
        UserDTO queryResult = userMapper.findUserByLoginId(loginId.get());

        return Optional.ofNullable(queryResult);
    }

    /**
     * 현재 유저 정보를 가져오는 메소드
     * @return Security Context의 유저 정보(userId, loginId, name, role)
     */
    public UserDTO getUserFromContext() {
        Optional<UserDTO> user = securityUtil.getCurrentUserInfo();

        if (user.isEmpty()) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST);
        }

        return user.orElse(null);
    }

    /**
     * 게시글의 작성자와 요청자가 일치하는지 확인하는 메소드
     * @param authorId 게시글 작성자 id
     */
    public void verifySameUser(Long authorId) {
        UserDTO user = getUserFromContext();

        if (!user.getUserId().equals(authorId)) {
            throw new CustomException(CustomErrorCode.DENIED_POST_REQUEST);
        }
    }
}
