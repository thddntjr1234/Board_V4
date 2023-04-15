package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface UserMapper {

    UserDTO findUserByUserId(String loginId);
}
