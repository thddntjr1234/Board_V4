import { useEffect, useState } from "react";

import { useRouter } from "next/router";

import * as boardApi from "@/apis/board";
import Navbar from "@/components/Navbar";
import ApiErrorHandler from "@/error/api-error-handler";
import { validateFormData } from "@/utils/validation";

const EditPost = () => {
  const route = useRouter();

  const [post, setPost] = useState();

  const [categoryId, setCategoryId] = useState();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  // 클라이언트 상에서 수정 요청 시 제출할 새로운 파일 리스트
  const [fileList, setFileList] = useState([]);
  const [commentList, setCommentList] = useState([]);
  const [categoryList, setCategoryList] = useState([]);

  // 서버 상에서 사용할 기존 파일 리스트
  const [existingFileList, setExistingFileList] = useState([]);

  /**
   * 수정 화면에서 사용할 게시글 정보
   * @returns post
   */
  const getPost = async () => {
    try {
      const response = await boardApi.getPost(`boards/free/${route.query.postId}`);

      // existingFileList의 크기를 3으로 고정
      let newfileList = response.data.fileList;

      if (response.data.fileList.length < 3) {
        let count = 3 - response.data.fileList.length;
        while(count--) {
          newfileList.push(null);
        }

        setExistingFileList(newfileList);
      }

      // 응답 데이터 추가
      setExistingFileList(response.data.fileList);
      setPost(response.data.post);
      setCategoryId(response.data.post.categoryId)
      setTitle(response.data.post.title);
      setContent(response.data.post.content);
      setCommentList(response.data.commentList);

    } catch (error) {
      await ApiErrorHandler(error);
      console.error("게시글 데이터를 받아오는 데 실패했습니다");
    }
  };

  /**
   * 게시글 폼에 바인딩할 카테고리 리스트 요청
   */
  const getCategoryList = async () => {
    try {
      const response = await boardApi.getCategoryList("boards/free/new");
      setCategoryList(response.data.categoryList);
    } catch (error) {
      await ApiErrorHandler(error);
      route.back();
    }
  };

  /**
   * 게시글 수정
   */
  const modifyPost = async () => {
    const formData = new FormData();

    // 게시글 데이터 입력
    for (const key in post) {
      if (post[key]) {
        formData.append(key, post[key]);
      }
    }
    formData.set("title", title);
    formData.set("categoryId", categoryId);
    formData.set("content", content);

    console.log(`수정될 게시글 내용 ${JSON.stringify(formData)}`);
    if (!validateFormData(formData)) {
      console.error("유효성 검증 실패");
      return;
    }

    // 신규 파일 입력
    for (let i = 0; i < fileList.length; i++) {
      if (fileList[i]) {
        console.log("i: " + i + ", 입력");
        formData.append("file", fileList[i]);
      }
    }

    // 기존 파일 리스트 입력
    const json = JSON.stringify(existingFileList);
    const blob = new Blob([json], { type: "application/json" });
    formData.append("existingFiles", blob);

    try {
      const response = await boardApi.modifyPost(`boards/free/${post.postId}`, formData);

      alert("게시글을 성공적으로 수정했습니다.");
      route.back();
    } catch (error) {
      await ApiErrorHandler(error);
    }
  };

  /**
   * 파일 추가 메소드
   * @param number 작업이 이뤄지는 파일 인덱스
   * @param event 입력한 파일이 존재하는 이벤트 객체
   */
  const addFile = (number, event) => {
    console.log(`number: ${number}`);
    const file = event.target.files[0];

    let newFileList = [...fileList];
    // 입력한 파일이 존재한다면 files[0]을, 아니면 null값을 입력
    // TODO: set 메소드가 있음에도 다이렉트로 값을 주입할 수 있다.
    newFileList[number] = file || null;

    // 변경한 파일 리스트를 적용
    setFileList(newFileList);
  };

  /**
   * 파일 삭제 메소드
   * @param number 파일 인덱스
   */
  const deleteFile = (number) => {
    let newExistingFileList = [...existingFileList];

    if (newExistingFileList[number]) {
      newExistingFileList[number] = null;
    }

    setExistingFileList(newExistingFileList);
  };

  useEffect(() => {
    getPost();
    getCategoryList();
  }, []);

  return (
    <>
      <Navbar />
      <div className="container">
        <h1>커뮤니티 - 수정</h1>
        <br />
        <div className="row mb-3">
          <label htmlFor="categoryId" className="col-sm-2 col-form-label">
            카테고리
          </label>
          <div className="col-sm-10">
            <select
              className="form-select"
              value={categoryId}
              onChange={(e) => {
                setCategoryId(e.target.value);
              }}
              required
            >
              <option disabled value="">
                카테고리 선택
              </option>
              {categoryList.map((category) => (
                <option key={category.categoryId} value={category.categoryId}>
                  {category.category}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="row mb-3">
          <label htmlFor="title" className="col-sm-2 col-form-label">
            제목
          </label>
          <div className="col-sm-10">
            <input
              type="text"
              className="form-control"
              id="title"
              value={title}
              onChange={(e) => {setTitle(e.target.value)}}
              placeholder="제목을 입력하세요"
              required
            />
          </div>
        </div>
        <div className="row mb-3">
          <label htmlFor="content" className="col-sm-2 col-form-label">
            내용
          </label>
          <div className="col-sm-10">
            <textarea
              className="form-control"
              id="content"
              name="content"
              rows="20"
              value={content}
              onChange={(e) => {setContent(e.target.value)}}
              placeholder="내용을 입력하세요"
              required
            />
          </div>
        </div>
        <div className="row mb-3">
          <label className="col-sm-2 col-form-label">파일 첨부</label>
          <div className="col-sm-10">
            {existingFileList.map((file, index) => (
              <div key={index} className="mb-3">
                {file ? (
                  <div className="input-group">
                    <span className="input-group-text">{file.fileRealName}</span>
                    <button type="button" className="btn btn-danger" onClick={() => deleteFile(index)}>
                      삭제
                    </button>
                  </div>
                ) : (
                  <input type="file" className="form-control" onChange={(e) => addFile(index, e)} />
                )}
              </div>
            ))}
          </div>
        </div>
        <div className="row">
          <div className="col-sm-10 offset-sm-2">
            <button className="btn btn-primary" onClick={modifyPost}>
              저장
            </button>
            <button className="btn btn-secondary me-2" onClick={() => route.back()}>
              취소
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default EditPost;
