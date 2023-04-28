package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.LoginDTO;
import com.ebstudy.board.v4.dto.TokenDTO;
import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.global.jwt.JwtFilter;
import com.ebstudy.board.v4.global.jwt.JwtProvider;
import com.ebstudy.board.v4.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    /**
     * 유효한 로그인 요청에 대해 JWT 발급
     * @param loginDto loginId, password
     * @return JWT
     */
    @PostMapping("/api/signin")
    public ResponseEntity<TokenDTO> signin(@Valid @RequestBody LoginDTO loginDto) {

        // Controller에 비즈니스 로직이 들어갔기는 한데 지금은 단일 메소드만 가지고 있으니 일단 이대로 사용
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/api/user")
    public ResponseEntity<Optional<UserDTO>> getMyUserInfo() {
        log.info("getMyUserInfo 수행");
        Optional<UserDTO> user = userService.getMyUserWithAuthorities();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/user/{loginId}")
    public ResponseEntity<Optional<UserDTO>> getUserInfo(@PathVariable String loginId) {
        log.info("getUserInfo 수행");
        Optional<UserDTO> user = userService.getUserWithAuthorities(loginId);

        return ResponseEntity.ok(user);
    }
}
