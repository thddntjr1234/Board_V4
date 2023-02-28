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
    // PostDTO vs 필요한 필드만 선언
//    private Long postId;
//    private Long hits;
//    private Long categoryId;
//    private String createdDate;
//    private String modifiedDate;
//    private String title;
//    private String content;
//    private String author;
//    private String category;
//    private String passwd;
    private List<FileDTO> fileList;
    private List<CommentDTO> commentList;

}
