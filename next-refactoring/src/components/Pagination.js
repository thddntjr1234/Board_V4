import {useRouter} from "next/router";

export default function Pagination({ pagingValues, pageRange }) {

  const route = useRouter();
  const movePage = (page) => {
    route.replace({
      pathname: route.basePath,
      query: {
        pageNumber: page,
        keyword: route.query.keyword,
        startDate: route.query.startDate,
        endDate: route.query.endDate,
        categoryId: route.query.categoryId,
        boardType: route.query.boardType,
      }
    }).then(() => {});
  }

  return (
    <div className="container d-flex justify-content-center">
      <ul className="pagination">
        {pagingValues.startPage > 1 && (
          <li className="page-item" onClick={() => movePage(1)}>
            <button className="page-link">처음</button>
          </li>
        )}
        {pagingValues.currentPage > 1 && (
          <li className="page-item" onClick={() => movePage(pagingValues.currentPage - 1)}>
            <button className="page-link">이전</button>
          </li>
        )}
        {pageRange.map((i) => (
          <li key={i} className={`page-item${i === pagingValues.currentPage ? ' active' : ''}`} onClick={() => movePage(i)}>
            <button className="page-link">{i}</button>
          </li>
        ))}
        {pagingValues.currentPage < pagingValues.totalPage && (
          <li className="page-item" onClick={() => movePage(pagingValues.currentPage + 1)}>
            <button className="page-link">다음</button>
          </li>
        )}
        {pagingValues.endPage < pagingValues.totalPage && (
          <li className="page-item" onClick={() => movePage(pagingValues.totalPage)}>
            <button className="page-link">끝</button>
          </li>
        )}
      </ul>
    </div>
  );
}