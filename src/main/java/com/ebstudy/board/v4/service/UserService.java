package com.ebstudy.board.v4.service;


import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.global.authority.Role;
import com.ebstudy.board.v4.global.util.SecurityUtil;
import com.ebstudy.board.v4.repository.UserMapper;
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

    @Transactional
    public UserDTO signup(UserDTO userDTO) throws DuplicateMemberException {
        if (userMapper.findUserByUserId(userDTO.getLoginId()) == null) {
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


    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthorities(String loginId) {
        return Optional.ofNullable(userMapper.findUserByUserId(loginId));
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getMyUserWithAuthorities() {

        // 기존 강의 코드: SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername
        Optional<String> loginId = SecurityUtil.getCurrentUsername();
        UserDTO queryResult = userMapper.findUserByUserId(String.valueOf(loginId));
        UserDTO user = UserDTO
                .builder()
                .loginId(queryResult.getLoginId())
                .role(queryResult.getRole())
                .build();

        log.info(user.toString());
        return Optional.ofNullable(user);
    }
}
