import {useEffect, useState} from "react";

import { useRouter } from "next/router";

import * as boardApi from "@/apis/board";
import Navbar from "@/components/Navbar";
import ApiErrorHandler from "@/error/api-error-handler";
import { validateFormData } from "@/utils/validation";

const NewPost = () => {
  const route = useRouter();
  const [categoryList, setCategoryList] = useState([]);
  const [categoryId, setCategoryId] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [file, setFile] = useState([]);

  /**
   * 입력한 게시글 데이터를 서버 상으로 저장하도록 요청
   */
  const savePost = async () => {
    const formData = new FormData();

    formData.append("title", title);
    formData.append("content", content);
    formData.append("categoryId", categoryId);

    if (!validateFormData(formData)) {
      return;
    }

    // 파일이 있다면 이를 일일히 append해야 리스트 단위로 들어가지 않는다.
    for (let i = 0; i < file.length; i++) {
      if (file[i]) { // TODO: 비어있는 지를 여기서 검증한다. -> addfile 내의 체크를 여기로 옮기기
        formData.append("file", file[i]);
      }
    }

    try {
      await boardApi.savePost("/boards/free", formData);
      alert("게시글 저장 성공");
      await route.push("/boards/free");
    } catch (error) {
      await ApiErrorHandler(error);
    }
  };

  /**
   * 게시글 폼에 바인딩할 데이터 요청
   */
  const getWriteFormData = async () => {
    try {
      const response = await boardApi.getWriteFormData("boards/free/new");
      setCategoryList(response.data.categoryList);
    } catch (error) {
      await ApiErrorHandler(error);
      await route.push("/boards/free");
    }
  };

  // 파일 추가 시 file 리스트에 추가
  const addFile = (number, event) => {
    const files = event.target.files;

    // 입력한 파일이 존재하지 않으면 null값 입력
    file[number] = files[0] || null; // TODO: (공유) file[number] 접근이 되는 이유
  };

  // 로딩 시 폼에 사용할 카테고리 리스트 요청
  useEffect(() => {
    getWriteFormData();
  }, []);

  return (
    <>
      <Navbar />
      <div className="container">
        <h1>커뮤니티 - 작성</h1>
        <br />
        <div className="row mb-3">
          <label htmlFor="categoryId" className="col-sm-2 col-form-label">
            카테고리
          </label>
          <div className="col-sm-10">
            <select id="categoryId" className="form-select" name="categoryId" value={categoryId} onChange={(e) => setCategoryId(e.target.value)} required>
              <option disabled value="">카테고리 선택</option>
              {categoryList.map((category) => (
                <option key={category.categoryId} value={category.categoryId}>
                  {category.category}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="row mb-3">
          <label htmlFor="title" className="col-sm-2 col-form-label">제목</label>
          <div className="col-sm-10">
            <input type="text" className="form-control" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="제목을 입력하세요" required/>
          </div>
        </div>
        <div className="row mb-3">
          <label htmlFor="content" className="col-sm-2 col-form-label">내용</label>
          <div className="col-sm-10">
            <textarea className="form-control" value={content} onChange={(e) => setContent(e.target.value)} rows="20" placeholder="내용을 입력하세요" required></textarea>
          </div>
        </div>
        <div className="row mb-3">
          <label className="col-sm-2 col-form-label">파일 첨부</label>
          <div className="col-sm-10">
            {[...Array(3)].map((_, index) => (
              <div className="mb-3" key={index}>
                <input type="file" className="form-control" onChange={(e) => addFile(index, e)} />
              </div>
            ))}
          </div>
        </div>
        <div className="row">
          <div className="col-sm-10 offset-sm-2">
            <button className="btn btn-secondary me-2" onClick={() => route.push("/boards/free")}>
              목록
            </button>
            <button className="btn btn-primary" onClick={() => savePost()}>
              저장
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default NewPost;
