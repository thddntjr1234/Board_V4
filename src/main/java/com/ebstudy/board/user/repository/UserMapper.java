package com.ebstudy.board.user.repository;

import com.ebstudy.board.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDTO findUserByLoginId(String loginId);

    UserDTO findUserByUserId(Long userId);
}
