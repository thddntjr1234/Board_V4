import {useEffect, useState} from "react";

import { useRouter } from "next/router";

import * as userApi from "@/apis/user";

import { checkValidToken } from "@/utils/token"
export default function Navbar() {
  const route = useRouter();

  const [showModal, setShowModal] = useState(false);
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [isValidToken, setIsValidToken] = useState(false);

  // /**
  //  * localStorage에서 jwt토큰이 존재하는지를 확인
  //  * @returns {boolean}
  //  */
  // const checkValidToken = () => {
  //   const token = localStorage.getItem("token");
  //   console.log(`checkValidToken Method Called : ${!!token}`);
  //   setcheckValidToken(!!token);
  // }

  /**
   * 로그인 수행 메소드
   * @returns {Promise<void>}
   */
  const signin = async () => {
    const data = {
      loginId: loginId,
      password: password,
    };

    console.log(`login data: ${JSON.stringify(data)}`);
    try {
      const response = await userApi.signin(data);
      console.log(response.data.token);
      await localStorage.setItem("token", response.data.token);
      alert("로그인에 성공했습니다.");
      location.reload();

    } catch (e) {
      alert("아이디 혹은 패스워드가 잘못되었습니다.");
    }
  };

  /**
   * 로그아웃시 jwt를 localstorage 에서 제거
   * @returns {Promise<void>}
   */
  const signout = async () => {
    console.log(localStorage.getItem("token"));
    if (checkValidToken) {
      await localStorage.removeItem("token");
      alert("로그아웃에 성공했습니다.");

      // 로그아웃 시 쿼리 파라미터를 제거한 경로로 리다이렉트
      const path = route.asPath.split("?")[0];
      location.replace(path);
    }
  };

  // 최초 로딩 시 checkValidToken을 수행해 isValidToken 값을 업데이트한다.
  useEffect(() => {
    console.log("checkValidToken을 수행한다.")
    setIsValidToken(checkValidToken);
  }, []); // useEffect()가 렌더링 시 두번씩 호출되는 것을 방지하지 위해 두번째 파라미터에 []을 추가

  return (
    <div className="container">
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div className="d-flex justify-content-between align-items-center w-100 mx-5">
          <span className="d-inline-block text-truncate me-4">
            <a className="nav-link text-white" href="/">
              메인 페이지
            </a>
          </span>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav flex-fill">
              <li className="nav-item">
                <a className="nav-link" href="/boards/notice">
                  공지사항
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/boards/qna">
                  Q&A
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/boards/free">
                  커뮤니티
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/boards/gallery">
                  이미지 갤러리
                </a>
              </li>
              <li className="nav-item">
                <a className="nav-link" href="/boards/inquiry">
                  1:1문의
                </a>
              </li>
            </ul>
            <ul className="navbar-nav">
              <li className="nav-item">
                {!isValidToken ? (
                  <button type="button" className="btn btn-primary" onClick={() => setShowModal(true)}>
                    로그인
                  </button>
                ) : (
                  <button type="button" className="btn btn-primary" onClick={signout}>
                    로그아웃
                  </button>
                )}
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <div>
        {/* 로그인 모달 */}
        {showModal && (
          <div className="modal" tabIndex="-1" role="dialog" style={{ display: "block" }}>
            <div
              className="modal-dialog modal-dialog-centered position-absolute"
              role="document"
              style={{ top: "50%", left: "50%", transform: "translate(-50%, -50%)" }}
            >
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">로그인</h5>
                  <button
                    type="button"
                    className="close"
                    data-dismiss="modal"
                    aria-label="Close"
                    onClick={() => setShowModal(false)}
                  >
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div className="modal-body">
                  {/* 로그인 폼 */}
                  <form>
                    <div className="form-group">
                      <label htmlFor="username">아이디</label>
                      <input
                        type="text"
                        className="form-control"
                        id="username"
                        value={loginId}
                        onChange={(e) => setLoginId(e.target.value)}
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="password">비밀번호</label>
                      <input
                        type="password"
                        className="form-control"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                      />
                    </div>
                  </form>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-primary" onClick={signin}>
                    로그인
                  </button>
                  <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                    취소
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}