package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.*;
import com.ebstudy.board.v4.global.validator.EqualEachPasswd;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@Mapper // SqlSession을 만들고 Mapper를 등록할 필요가 없이 이 어노테이션으로 해결 가능
public interface BoardMapper {

    List<PostDTO> getPostList(PaginationDTO paginationValues);

    int getPostCount(SearchDTO searchValues);

    PostDTO getPost(Long postId);

    List<CategoryDTO> getCategoryList();

    long savePost(PostDTO post);

    void increaseHits(long postId);

    void updatePost(@EqualEachPasswd({"passwd", "confirmPasswd"}) PostDTO post);

     // post는 postId, passwd, confirmPasswd 값만을 보유함
    void deletePost(@EqualEachPasswd({"passwd", "confirmPasswd"}) PostDTO post);
}
