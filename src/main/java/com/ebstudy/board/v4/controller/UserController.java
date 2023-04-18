package com.ebstudy.board.v4.controller;

import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Optional<UserDTO>> getMyUserInfo() {
        Optional<UserDTO> user = userService.getMyUserWithAuthorities();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/user/{loginId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(loginId).get());
    }
}
