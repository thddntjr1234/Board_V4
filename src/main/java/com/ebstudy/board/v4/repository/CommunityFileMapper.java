package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper
public interface CommunityFileMapper {

    /**
     * 해당 postId값의 게시글에 종속된 파일의 리스트를 반환
     */
    List<FileDTO> getFileList(long postId);

    /**
     * 요청받은 파일의 정보를 저장
     */
    void saveFile(FileDTO file);

    /**
     * 요청받은 파일을 삭제
     */
    void deleteFile(Long fileId);
}
