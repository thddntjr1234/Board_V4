/**
 * 게시글 리스트 날짜 포맷 변환
 * @param list 변환할 게시글 리스트
 * @returns {*}
 */
const convertPostListDateFormat = (list) => {
    list.forEach(obj => convertDateFormat(obj))

    return list
}

/**
 * 게시글 날짜 포맷 변환
 * @param obj
 * @returns {*}
 */
function convertPostDateFormat(obj) {
    return convertDateFormat(obj)
}

/**
 * 날짜 포맷 변환 메소드
 * @param obj
 * @returns {*}
 */
const convertDateFormat = (obj) => {

    // createdDate 변환
    if (obj.createdDate) {
        const dateParts = obj.createdDate.split(" ");
        obj.createdDate = dateParts[0] + " " + dateParts[1].split(":").slice(0, 2).join(":");
    }

    // modifiedDate 변환
    if (obj.modifiedDate) {
        const dateParts = obj.modifiedDate.split(" ");
        obj.modifiedDate = dateParts[0] + " " + dateParts[1].split(":").slice(0, 2).join(":");
    }

    return obj
}

/**
 * 댓글 리스트의 내용과 작성일자, 수정일자의 포맷을 변환한다.
 * @param list
 */
function convertCommentListDataFormat(list) {
    list.forEach(comment => {
        convertNewlineCharacter(comment)
        convertDateFormat(comment)
    })

    return list
}

function convertNewlineCharacter(comment) {
    comment.comment = comment.comment.replace(/\n/g, '<br>')
    return comment
}

export { convertPostListDateFormat, convertCommentListDataFormat, convertNewlineCharacter, convertDateFormat, convertPostDateFormat}