import { useEffect, useState } from "react";

import { useRouter } from "next/router";

import * as boardApi from "@/apis/board";
import * as userApi from "@/apis/user";
import Comment from "@/components/Comment";
import Navbar from "@/components/Navbar";
import ApiErrorHandler from "@/error/api-error-handler";
import { convertPostFormat, convertCommentListDataFormat } from "@/utils/format-converter";
import { checkValidToken } from "@/utils/token";

const FreeBoardPost = () => {
  const route = useRouter();
  const [post, setPost] = useState({});
  const [fileList, setFileList] = useState([]);
  const [commentList, setCommentList] = useState([]);
  const [isAuthor, setIsAuthor] = useState(false); // TODO: 자료형별 네이밍 규칙 준수
  const [currentUserInfo, setCurrentUserInfo] = useState({});

  /**
   * 게시글 요청 및 게시글 저자 여부 플래그 설정
   * @returns post
   */
  const getPost = async () => {
    try {
      // TODO: 굳이 getPost와 토큰에서 유저 정보를 조회하는 것을 분리시킬 필요가 있는가? => 없다.
      const response = await boardApi.getPost(`boards/free/${route.query.postId}`);

      setPost(convertPostFormat(response.data.post));
      setFileList(response.data.fileList);
      setCommentList(convertCommentListDataFormat(response.data.commentList));

      console.log(`게시글 콘텐츠 내용: ${post.content}`);
      // 수정 삭제 버튼을 보여주기 위한 flag 변수 처리
      if (checkValidToken()) {
        console.log("유효성 검증 통과")
        const authorityResponse = await userApi.getMyInfo();

        setCurrentUserInfo(authorityResponse.data);
        if (authorityResponse.data.userId === response.data.post.authorId) {
          console.log("게시글의 주인입니다.")
          setIsAuthor(true);
        }
      }
    } catch (error) {
      await ApiErrorHandler(error);
      console.error("게시글 데이터를 받아오는 데 실패했습니다");
    }
  };

  /**
   * 게시글 삭제 메소드
   */
  const deletePost = async () => {
    console.log("deletePost() called")
    try {
      await boardApi.deletePost(`boards/free/${post.postId}`);
      alert("게시글을 삭제하는 데 성공했습니다.");
      route.back(); // TODO: replcae를 해야 history에 남지 않는다.(목록으로 push)
    } catch (error) {
      await ApiErrorHandler(error);
    }
  };

  /**
   * 요청한 파일에 대해 다운로드를 수행하는 메소드
   * @param file
   * @returns 다운로드를 요청한 파일
   */
  const downloadFile = async (file) => {
    try {
      const response = await boardApi.downloadFile("boards/free/file", file);
      const blob = new Blob([response.data], { type: response.headers["content-type"] });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");

      link.href = url;
      link.download = file.fileRealName;
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      await ApiErrorHandler(error);
    }
  };

  /**
   * 이전 페이지(목록)으로 이동
   */
  const backToList = () => {
    route.back();
  };

  /**
   * 게시글 수정 페이지로 이동
   */
  const moveToModifyView = () => {
    console.log("route path: " + route.path + "/edit");
    route.push(`${route.asPath}/edit`);
  };

  useEffect(() => {
    if (route.isReady) {
      console.log("getPost() 수행");
      getPost();
    }
  }, [route.isReady]); // route.query가 빈 값으로 전달되는 것을 막기 위해 isReady 시 getPost()를 수행한다.

  return (
    <div style={{ marginTop: "80px" }}>
      <Navbar></Navbar>

      <div className="container">
        <h1 className="mt-4 text-center">커뮤니티 - 게시글</h1>
        <div className="row mb-3">
          <div className="col-sm-3 text-start">
            <span className="fw-bold">{post.author}</span>
          </div>
          <div className="col-sm-9 text-end">
            <span className="ms-4">등록일시: {post.createdDate}</span>
            {post.modifiedDate && <span className="ms-4">수정일시: {post.modifiedDate}</span>}
          </div>
        </div>

        <div className="row mb-3">
          <div className="col-sm-9 text-start">
            <span className="fw-bold">
              [{post.category}] {post.title}
            </span>
          </div>
          <div className="col-sm-3 text-end">
            <span>조회수: {post.hits}</span>
          </div>
        </div>

        <hr className="mb-4" />

        <div className="row mb-4">
          <div className="col">
            <div className="min-vh-50">
              <textarea className="form-control-plaintext mb-3" rows="15" readOnly value={post.content} />
            </div>
          </div>
        </div>
        <hr className="row mb-4" />

        <div className="row mb-3">
          <div className="col-sm-3">
            <span className="fw-bold">첨부파일</span>
          </div>
          <div className="col-sm-9">
            {fileList.map((file, index) => (
              <div key={index}>
                <a key={index} href={"#"} onClick={() => downloadFile(file)}>
                  {file.fileRealName}
                </a>
              </div>
            ))}
          </div>
        </div>

        <hr className="row mb-4" />
        <Comment currentUserInfo={currentUserInfo} postId={post.postId} commentList={commentList} boardName={"free"} />
        {/*<Comment :current-user-info="currentUserInfo" :comment-list="commentList" @addComment="addComment" @modifyComment="modifyComment" @deleteComment="deleteComment"></Comment>*/}

        <div className="d-flex justify-content-center">
          <button className="btn btn-secondary me-3" onClick={() => backToList()}>
            목록
          </button>
          {isAuthor && (
            <>
              <button className="btn btn-secondary me-3" onClick={() => moveToModifyView()}>
                수정
              </button>
              <button className="btn btn-secondary" onClick={() => deletePost()}>
                삭제
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default FreeBoardPost;
