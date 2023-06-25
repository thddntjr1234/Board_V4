/**
 * 게시글 리스트 날짜 포맷 변환
 * @param list 변환할 게시글 리스트
 * @returns {*}
 */
const convertPostListFormat = (list) => {
    list.forEach(obj => {
        convertDateFormat(obj)
        restrictPostTitleLength(obj)
    })

    return list
}

/**
 * 게시글 날짜 포맷 변환
 * @param obj
 * @returns {*}
 */
function convertPostFormat(obj) {
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
 * 게시글 제목의 길이가 40자 이상이면 40자까지 끊은뒤 '...'문자열을 추가하여 변환
 * @param obj
 * @returns {*}
 */
const restrictPostTitleLength = (obj) => {
    if (obj.title.length >= 40) {
        obj.title = obj.title.substring(0, 40) + '...'
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

/**
 * 댓글의 개행문자를 <br> html태그로 변환한다.
 * @param comment
 * @returns {*}
 */
function convertNewlineCharacter(comment) {
    comment.comment = comment.comment.replace(/\n/g, '<br>')
    return comment
}

export { convertPostListFormat, convertCommentListDataFormat, convertNewlineCharacter, convertDateFormat, convertPostFormat}