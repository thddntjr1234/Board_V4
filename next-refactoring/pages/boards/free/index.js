
import {useEffect, useState} from "react";

import { useRouter } from "next/router";

import * as boardApi from "@/apis/board";
import BoardPostList from "@/components/BoardPostList";
import Navbar from "@/components/Navbar";
import Pagination from "@/components/Pagination";
import {convertPostListFormat} from "@/utils/format-converter";
import {checkValidToken} from "@/utils/token";

const FreeBoardList = () => {
  const route = useRouter();

  const boardName = 'free';
  const [postList, setPostList] = useState([]);
  const [noticeList, setNoticeList] = useState([]);

  const [pagingValues, setPagingValues] = useState({});
  const [pageRange, setPageRange] = useState([]);
  const [categoryList, setCategoryList] = useState([]);

  // 사용하지 않음
  const [pageNumber, setPageNumber] = useState(route.query.pageNumber || null);
  const [keyword, setKeyword] = useState(route.query.keyword || null);
  const [startDate, setStartDate] = useState(route.query.startDate || null);
  const [endDate, setEndDate] = useState(route.query.endDate || null);
  const [categoryId, setCategoryId] = useState(route.query.categoryId || "");

  // 동적 컴포넌트 사용시 사용할 state
  const [currentPostListComponenet, setCurrentPostListComponent] = useState();

  /**
   * api 서버로 게시글 리스트를 전송받아 관련 데이터를 설정하는 메소드
   */
  const getPostList = async () => {
    const response = await boardApi.getPostList("boards/free", {
      pageNumber: route.query.pageNumber,
      categoryId: route.query.categoryId,

      // spread를 사용하지 않으면 String 타입이라 ""값이 전송돼서 mybatis if문에서 제대로 걸러지지 않는다.
      // 반면 spread를 사용하면 쿼리 파라미터가 존재하지 않을 때 null값을 반환시킨다.
      ...(route.query.keyword && { keyword: route.query.keyword }),
      ...(route.query.startDate && { startDate: route.query.startDate }),
      ...(route.query.endDate && { endDate: route.query.endDate }),
      ...(route.query.filter && { filter: route.query.filter }),
      ...(route.query.secret && { secret: route.query.secret }),
      ...(route.query.sort && { sort: route.query.sort }),
    });

    console.log(`쿼리 파라미터 정보${JSON.stringify(route.query)}`);
    // console.log('response data: ' + JSON.stringify(response.data));
    setPagingValues(response.data.pagingValues);

    // 배열 초기화
    setPageRange([null]);

    // pageRange 값 설정
    let newArr = [];
    //
    for (let i = response.data.pagingValues.startPage; i <= response.data.pagingValues.endPage; i++) {
      newArr.push(i);
      console.log(i);
    }
    setPageRange(newArr);

    console.log(`pageRange: ${newArr}`)

    // 데이터 입력
    setPostList(convertPostListFormat(response.data.postList));
    setCategoryList(response.data.categoryList);
  };

  /**
   * 상단 고정 공지사항 게시글 리스트를 가져와 설정한다.
   * @returns {Promise<void>}
   */
  //TODO: 함수명을 고려
  const getFixedNoticeList = async () => {
    const response = await boardApi.getFixedNoticeList("community");
    setNoticeList(convertPostListFormat(response.data));
  };

  /**
   * 게시글 작성 화면으로 이동하는 메소드
   */
  const moveToWriteView = async () => {
    if (!checkValidToken) {
      alert("게시글 작성은 회원만 가능합니다.");
    } else {
      await route.push("/boards/free/new");
    }
  };

  /**
   * 뷰 모드를 전환하는 메소드
   * @param component
   */
  const togglePostListComponent = (component) => {
    // 쿼리 파라미터용으로 사용할 변수
    // getBoardComponentByName(component)
    route.replace({ path: route.asPath, query: { ...route.query, boardType: component }});
  };

  /**
   * 컴포넌트 이름을 실제 컴포넌트로 주입하는 메소드
   * @param componentName 컴포넌트 이름 문자열
   */
  const getBoardComponentByName = (componentName) => {
    const props = {
      boardName: boardName,
      postList: postList,
      noticeList: noticeList
    }

    switch (componentName) {
      case "list":
        setCurrentPostListComponent(BoardPostList(props));
        break;
      case "card":
        // setCurrentPostListComponenet(CardPostList);
        break;
    }
  };

  useEffect(() => {
    getFixedNoticeList().then(r => {});
    getPostList();
    getBoardComponentByName(route.query.boardType);
    // ex) [pageNumber, categoryId, startDate, endDate, keyword]
  }, [route.query]);

  return (
    <div style={{marginTop: "80px"}}>
      <Navbar></Navbar>

      {/*상단 네비게이션 바*/}
      <div className="container text-center">
        <h1>커뮤니티 - 목록</h1>
      </div>

      {/*검색*/}
      <div className="container" style={{marginTop: "50px"}}>
        <form className="form-inline">
          <div className="input-group input-group-sm">
            <span className="input-group-text">등록일</span>
            <input name="startDate" className="form-control" type="date"/>
            <input name="endDate" className="form-control" type="date"/>

            <select className="form-select" name="categoryId">
              <option value="">전체 카테고리</option>
              {categoryList.map((category) => (
                <option key={category.categoryId} value={category.categoryId}>{ category.category }</option>
              ))}
            </select>
            <input type="search" name="keyword" className="form-control" placeholder="제목/내용/작성자명 키워드" />
            <button className="btn btn-primary" type="submit">검색</button>
          </div>
        </form>
      </div>
      <br/>

      <div className="container" style={{marginBottom: "30px"}}>
        <div className="d-flex justify-content-between">
          <button className="btn btn-primary" onClick={() => {moveToWriteView()}}>게시글 등록</button>

          <div className="btn-group" style={{marginLeft: "auto"}}>
            <button className="btn btn-outline-primary" onClick={() => {togglePostListComponent('list')}}>리스트</button>
            <button className="btn btn-outline-primary" onClick={() => {togglePostListComponent('card')}}>카드</button>
          </div>
        </div>
      </div>

      <div className="container">
        <BoardPostList boardName={boardName} postList={postList} noticeList={noticeList}></BoardPostList>
      </div>

      <div className="d-flex justify-content-betweeen">
        <Pagination pagingValues={pagingValues} pageRange={pageRange}></Pagination>
      </div>

    </div>
  );
};

export default FreeBoardList;
