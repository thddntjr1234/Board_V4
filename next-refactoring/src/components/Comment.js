import {useEffect, useState} from "react";

import Router from "next/router";
import * as boardApi from "../apis/board";
import {useRouter} from "next/router";

import ApiErrorHandler from "@/error/api-error-handler";


export default function Comment({currentUserInfo, postId, commentList, boardName}) {
  const [newComment, setNewComment] = useState({});
  const [editingComment, setEditingComment] = useState({});
  const [editStatusOfComment, setEditStatusOfComment] = useState();
  const route = useRouter();

  const addCommnet = async (comment) => {
    const data = {
      postId: postId,
      comment: comment
    }

    try {
      await boardApi.addComment(`boards/${boardName}/comment`, data)
      alert("댓글을 성공적으로 등록했습니다.");
      route.reload();
    } catch (error) {
      await ApiErrorHandler(error);
    }
  }

  const deleteComment = async (comment) => {
    try {
      await boardApi.deleteComment(`boards/${boardName}/comment/${comment.commentId}`);
      alert("댓글을 성공적으로 삭제했습니다");
      route.reload();
    } catch (error) {
      await ApiErrorHandler(error);
    }
  }

  const modifyComment = async (comment) => {
    console.log(`comment: ${comment.comment}`);
    try {
      console.log("comment" + JSON.stringify(comment));
      await boardApi.modifyComment(`boards/${boardName}/comment`, comment);
      alert("댓글을 성공적으로 수정했습니다.");
      route.reload();

    } catch (error) {
      await ApiErrorHandler(error);
    }
  }

  const enableEditMode = (comment) => {
    // <br>태그로 변환했던 내용을 다시 원래 개행문자로 되돌린다.
    let convertedComment = {...comment};
    convertedComment.comment = comment.comment.replace(/<br>/g, "\n");

    setEditingComment(convertedComment);
    setEditStatusOfComment(comment.commentId);
  }

  const cancelEdit = () => {
    let originComment = { ...editingComment };
    originComment.comment = originComment.comment.replace(/\n/g, "<br>");

    setEditingComment(originComment);
    setEditStatusOfComment(null);
  }

  // Q&A 전용
  // const adoptComment = () => {
  //
  // }

  useEffect(() => {

  }, [editingComment]);

  return (
    <div className="container">
      <form className="mb-3">
        <textarea className="form-control mb-3" rows="3" onChange={(e) => setNewComment(e.target.value)}></textarea>
        <div className="d-flex justify-content-end">
          <button className="btn btn-primary" type="button" onClick={() => addCommnet(newComment)}>등록</button>
        </div>
      </form>

      <div className="p-3 mb-3 min-vh-50">
        {commentList.map((comment, index) => ( // TODO: 댓글 리스트만 별도로 가져오도록 api를 만들면 렌더링을 더 좋게 할 수 있음 +  key값은 고유 식별이 가능한 값이여야 한다(ex: pk)
          <div key={index}>
            <div className="row mb-3">
              <div className="col-sm-9 text-start">
                <span style={{paddingRight: "10px"}}>{ comment.username }</span>
                <span style={{fontSize: "11px"}}>{ comment.createdDate } 작성</span>
              </div>
              <div className="col-sm-3 text-end">
                {(!editStatusOfComment && comment.userId === currentUserInfo.userId) && (
                  <>
                    <button className="btn btn-outline-primary" onClick={() => enableEditMode(comment)}>수정</button>
                    <button className="btn btn-outline-secondary" onClick={() => deleteComment(comment)}>삭제</button>
                  </>
                )}
              </div>
            </div>
            {(!editStatusOfComment || editStatusOfComment !== comment.commentId) && ( // TODO: 렌더링 조건식을 좀 더 명확하게 지정
              <div className="col-sm-12 text-start">
                <div dangerouslySetInnerHTML={{ __html: comment.comment }}></div>
              </div>
            )}
            {(editStatusOfComment && editStatusOfComment === comment.commentId) && (
              <>
                <div className="col-sm-12">
                  <textarea className="form-control mb-3" rows="3" value={editingComment.comment} onChange={(e) => {
                    let newEditingComment = { ...editingComment };
                    newEditingComment.comment = e.target.value;
                    setEditingComment(newEditingComment);
                  }}></textarea>
                  <button className="btn btn-primary" type="button" onClick={() => modifyComment(editingComment)}>저장</button>
                  <button className="btn btn-outline-secondary" type="button" onClick={() => cancelEdit()}>취소</button>
                </div>
              </>
            )}
            <hr/>
          </div>
        ))}
      </div>
    </div>
  );
}