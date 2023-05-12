package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDTO findUserByLoginId(String loginId);

    UserDTO findUserByUserId(Long userId);
}
