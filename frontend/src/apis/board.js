import {authApiService, nonAuthApiService} from "@/apis/service"
import {useRoute} from "vue-router";
import {head} from "axios";

/**
 * 게시글 리스트를 요청하는 API
 * @param url 요청할 게시판의 URI 주소
 * @returns postList, pagingValues, categoryList
 */
function getPostList(url) {
    const route = useRoute()

    return nonAuthApiService.get(url, {
        params: {
            pageNumber: route.query.pageNumber,
            categoryId: route.query.categoryId,

            // spread를 사용하지 않으면 String 타입이라 ""값이 전송돼서 mybatis if문에서 제대로 걸러지지 않는다.
            // 반면 spread를 사용하면 쿼리 파라미터가 존재하지 않을 때 null값을 반환시킨다.
            ...(route.query.keyword && {keyword: route.query.keyword}),
            ...(route.query.startDate && {startDate: route.query.startDate}),
            ...(route.query.endDate && {endDate: route.query.endDate}),
        }
    })
}

/**
 * 게시글을 요청하는 API
 * @param url 요청할 게시글의 URI
 * @returns post, commentList, fileList
 */
function getPost(url) {
    return nonAuthApiService.get(url)
}

/**
 * 카테고리 리스트를 요청하는 API
 * @param url 카테고리 요청 URI
 * @returns categoryList
 */
function getCategoryList(url) {
    return authApiService.get(url)
}

function getWriteFormData(url) {
    return authApiService.get(url)
}

/**
 * 게시글 수정을 요청하는 API
 * @param url 수정할 게시글의 URI
 * @param formData 수정할 게시글 내용
 * @returns CommonResponsseDTO
 */
function modifyPost(url, formData) {
    return authApiService.put(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    })
}

function savePost(url, formData) {
    return authApiService.post(url, formData, {
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
    return authApiService.delete(url)
}

/**
 * 파일 다운로드를 요청하는 API
 * @param url 다운로드를 요청할 파일의 기본 URI
 * @param file 쿼리 파라미터로 추가할 파일 정보
 * @returns 다운로드 할 파일
 */
function downloadFile(url, file) {
    return nonAuthApiService.get(url, {
        params: {
            fileName: file.fileName,
            fileRealName: file.fileRealName
        },
        responseType: "blob",
    })
}

/**
 * 댓글 작성을 요청하는 API
 * @param url 댓글 작성 URI
 * @param data 댓글 정보
 * @returns CommonResponseDTO
 */
function addComment(url, data) {
    return authApiService.post(url, data)
}

/**
 * 게시글 수정 요청 API
 * @param url 수정 요청할 URI
 * @param data 수정 요청할 댓글 내용
 * @returns CommonResponseDTO
 */
function modifyComment(url, data) {
    return authApiService.put(url, data)
}

/**
 * 게시글 삭제 요청 API
 * @param url 삭제 요청할 URI
 * @returns CommonResponseDTO
 */
function deleteComment(url) {
    return authApiService.delete(url)
}

function adoptComment(url, post) {
    return authApiService.put(url, post)
}

export {
    getPostList,
    getPost,
    getCategoryList,
    getWriteFormData,

    savePost,
    modifyPost,
    deletePost,

    downloadFile,

    addComment,
    modifyComment,
    deleteComment,
    adoptComment,
}