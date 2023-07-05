import apiService from "@/apis/service"

/**
 * 게시글 리스트를 요청하는 API
 * @param url 요청할 게시판의 URI 주소
 * @param params 검색 조건 파라미터
 * @returns postList, pagingValues, categoryList
 */
function getPostList(url, params) {
    return apiService.get(url, {params})
}

function getFixedNoticeList(board) {
    return apiService.get('/boards/notice/fix', {
        params: {
            target: board
        }
    })
}

/**
 * 게시글을 요청하는 API
 * @param url 요청할 게시글의 URI
 * @returns post, commentList, fileList
 */
function getPost(url) {
    // 라우팅 방식으로 게시글 페이지로 이동하는 방식에선 비밀글만
    if (url.includes('inquiry')) {
        return apiService.get(url)
    }

    return apiService.get(url)
}

/**
 * 이미지 갤러리 게시글 수정 시 게시글 정보와 파일 정보를 함께 가져오는 API
 * @param url 요청할 게시글 URI
 * @returns post, fileList
 */
function getPostWithFileList(url) {
    return apiService.get(url)
}

/**
 * 카테고리 리스트를 요청하는 API
 * @param url 카테고리 요청 URI
 * @returns categoryList
 */
function getCategoryList(url) {
    return apiService.get(url)
}

function getWriteFormData(url) {
    return apiService.get(url)
}

/**
 * 게시글 수정을 요청하는 API
 * @param url 수정할 게시글의 URI
 * @param formData 수정할 게시글 내용
 * @returns CommonResponsseDTO
 */
function modifyPost(url, formData) {
    return apiService.put(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    })
}

function savePost(url, formData) {
    return apiService.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    })
}

/**
 * 게시글 삭제를 요청하는 API
 * @param url 삭제할 게시글의 URI
 * @returns CommonResponseDTO
 */
function deletePost(url) {
    return apiService.delete(url)
}

/**
 * 파일 다운로드를 요청하는 API
 * @param url 다운로드를 요청할 파일의 기본 URI
 * @param file 쿼리 파라미터로 추가할 파일 정보
 * @returns 다운로드 할 파일
 */
function downloadFile(url, file) {
    return apiService.get(url, {
        params: {
            fileName: file.fileName,
            fileRealName: file.fileRealName
        },
        responseType: "blob",
    })
}

/**
 *
 */
function saveImage(formData) {
    return apiService.post('boards/gallery/file', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    })
}

/**
 * 댓글 작성을 요청하는 API
 * @param url 댓글 작성 URI
 * @param data 댓글 정보
 * @returns CommonResponseDTO
 */
function addComment(url, data) {
    return apiService.post(url, data)
}

/**
 * 게시글 수정 요청 API
 * @param url 수정 요청할 URI
 * @param data 수정 요청할 댓글 내용
 * @returns CommonResponseDTO
 */
function modifyComment(url, data) {
    return apiService.put(url, data)
}

/**
 * 게시글 삭제 요청 API
 * @param url 삭제 요청할 URI
 * @returns CommonResponseDTO
 */
function deleteComment(url) {
    return apiService.delete(url)
}

function adoptComment(url, post) {
    return apiService.put(url, post)
}

export {
    getPostList,
    getPost,
    getCategoryList,
    getWriteFormData,
    getFixedNoticeList,
    getPostWithFileList,

    savePost,
    modifyPost,
    deletePost,

    downloadFile,
    saveImage,

    addComment,
    modifyComment,
    deleteComment,
    adoptComment,
}