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
    private PostDTO post;
    private List<FileDTO> fileList;
    private List<CommentDTO> commentList;

}
