package com.ebstudy.board.v4.gallery.repository;

import com.ebstudy.board.v4.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GalleryFileMapper {

    /**
     * 해당 postId값의 게시글에 종속된 파일의 리스트를 반환
     */
    List<FileDTO> getFileList(long postId);

    /**
     * 요청받은 파일의 정보를 저장
     */
    Long saveFile(FileDTO file);

    /**
     * 요청받은 파일을 삭제
     */
    void deleteFile(Long fileId);

    /**
     * 파일에 게시글 ID값을 추가
     */
    void updatePostIdFromFile(@Param("fileId") Long fileId, @Param("postId") Long postId);

    /**
     * 게시글 ID와 함께 썸네일 파일 저장
     */
    void saveThumbnail(FileDTO file);
}
