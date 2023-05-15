package com.ebstudy.board.v4.service;

import com.ebstudy.board.v4.dto.CommentDTO;
import com.ebstudy.board.v4.dto.UserDTO;
import com.ebstudy.board.v4.repository.CommunityCommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityCommentService {

    private final CommunityCommentMapper commentMapper;
    private final UserService userService;

    /**
     * 게시글 id에 해당하는 댓글 리스트를 가져오는 메소드
     * @param postId 가져올 댓글의 게시글 id
     * @return 댓글 리스트
     */
    public List<CommentDTO> getCommentList(Long postId) {
        return convertCommentListData(commentMapper.getCommentList(postId));
    }

    /**
     * 게시글 리스트의 생성시간 값을 요구 포맷에 맞게 변환하는 메소드
     * @param commentDTOList 댓글 리스트
     * @return 변환된 댓글 리스트
     */
    private List<CommentDTO> convertCommentListData(List<CommentDTO> commentDTOList) {

        List<CommentDTO> result = new LinkedList<>();

        for (CommentDTO comment : commentDTOList) {

            String createdDate = comment.getCreatedDate();

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.parse(createdDate, format);

            comment.setCreatedDate(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            result.add(comment);
        }

        return result;
    }

    /**
     * 댓글 저장 메소드
     * @param comment 저장할 댓글
     */
    public void saveComment( CommentDTO comment) {
        UserDTO user = userService.getUserFromContext();

        comment.setUserId(user.getUserId());
        comment.setCreatedDate(String.valueOf(LocalDateTime.now()));
        commentMapper.saveComment(comment);
    }

    /**
     * 댓글 삭제 메소드
     * @param commentId 삭제할 댓글의 Id
     */
    public void deleteComment(Long commentId) {
        UserDTO user = userService.getUserFromContext();

        CommentDTO originComment = commentMapper.findCommentByCommentId(commentId);

        if (user.getUserId().equals(originComment.getUserId())) {
            commentMapper.deleteComment(commentId);
        }
    }

    /**
     * 댓글 수정 메소드
     * @param comment 수정할 댓글
     */
    public void updateComment(CommentDTO comment) {
        UserDTO user = userService.getUserFromContext();

        CommentDTO originComment = commentMapper.findCommentByCommentId(comment.getCommentId());

        if (user.getUserId().equals(originComment.getUserId())) {
            comment.setModifiedDate(String.valueOf(LocalDateTime.now()));
            commentMapper.updateComment(comment);
        }
    }
}
