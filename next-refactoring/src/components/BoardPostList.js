import Link from "next/link";

export default function BoardPostList({ boardName, postList, noticeList }) {

  return (
    <div className="container">
      <table className="table table-hover" style={{ textAlign: 'center', fontSize: '12px' }}>
        <thead>
        <tr>
          {(boardName === 'qna' || boardName === 'free') && (
            <>
              <th className="w-auto" style={{ textAlign: 'center' }}>카테고리</th>
              {boardName === 'qna' && <th className="w-auto" style={{ textAlign: 'center' }}>채택여부</th>}
              <th className="w-auto" style={{ textAlign: 'center' }}>&nbsp;</th>
            </>
          )}
          {boardName === 'inquiry' && (
            <th className="w-auto" style={{ textAlign: 'center' }}>답변여부</th>
          )}
          <th className="w-50" style={{ textAlign: 'center' }}>제목</th>
          <th className="w-auto" style={{ textAlign: 'center' }}>작성자</th>
          <th className="w-auto" style={{ textAlign: 'center' }}>조회수</th>
          <th className="w-auto" style={{ textAlign: 'center' }}>등록 일시</th>
          <th className="w-auto" style={{ textAlign: 'center' }}>수정 일시</th>
        </tr>
        </thead>
        <tbody>
          {/*공지사항 게시글 렌더링*/}
          {noticeList.map((notice) => (
            <tr key={notice.postId} className={"table table-primary"}>
              {(boardName === 'qna' || boardName === 'free' || boardName === 'inquiry') && (
                <>
                  <td>[공지사항]</td>
                  {['qna'].includes(boardName) && <td></td>}
                  {['qna', 'free'].includes(boardName) && <td></td>}
                </>
              )}
              <td style={{ textAlign: 'justify' }}>
                <Link href={`/boards/notice/${notice.postId}`}>
                  {notice.title}
                  {notice.commentCount ? `[${notice.commentCount}]` : ''}
                </Link>
              </td>
              <td>{notice.author}</td>
              <td>{notice.hits}</td>
              <td>{notice.createdDate}</td>
              <td>{notice.modifiedDate}</td>
            </tr>
          ))}
          {/*각 게시판별 렌더링*/}
          {postList.map((post) => (
            <tr key={post.postId}>
              {(boardName === 'qna' || boardName === 'free') && (
                <>
                  <td>{post.category}</td>
                  {boardName === 'qna' && (
                      <td>
                        <span>{post.adoptedCommentId ? '✅' : ' '}</span>
                      </td>
                  )}
                  <td>
                    <span>{post.fileFlag ? '📁' : ' '}</span>
                  </td>
                </>
              )}
              {boardName === 'inquiry' && (
                <td>
                  <span>{post.answerStatus ? '답변 완료' : '답변 대기중'}</span>
                </td>
              )}
              <td style={{ textAlign: 'justify' }}>
                <span>{post.secret ? '🔒' : ' '}</span>
                <Link className="text-reset" href={`/boards/${boardName}/${post.postId}`}>
                  {post.title}
                </Link>
                <span>{post.commentCount ? ` [${post.commentCount}]` : ''}</span>
              </td>
              <td>{post.author}</td>
              <td>{post.hits}</td>
              <td>{post.createdDate}</td>
              <td>{post.modifiedDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}