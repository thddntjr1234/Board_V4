package com.ebstudy.board.v4.dto.response;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.FileDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PostResponseDTO {
    private PostDTO post; // 요청한 게시글 데이터
    private List<FileDTO> fileList; // 게시글에 종속된 파일 리스트
    private List<CommentDTO> commentList; // 게시글에 종속된 댓글 리스트

}
