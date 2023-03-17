package com.ebstudy.board.v4.repository;

import com.ebstudy.board.v4.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Mapper
public interface FileMapper {
    List<FileDTO> getFileList(long postId);

    void saveFile(FileDTO file);

    void deleteFile(Long fileId);
}
