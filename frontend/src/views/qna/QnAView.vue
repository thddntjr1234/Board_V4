<template>
  <NavBar></NavBar>
  <div class="container">
    <h1 class="mt-4 justify-content-start">Q&A - 게시글</h1>
    <div class="row mb-3">
      <div class="col-sm-3 text-start">
        <span class="fw-bold">{{ post.author }}</span>
      </div>
      <div class="col-sm-9 text-end">
        <span class="ms-4">등록일시: {{ post.createdDate }}</span>
        <span class="ms-4" v-if="post.modifiedDate !== null">수정일시: {{ post.modifiedDate }}</span>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col-sm-9 text-start">
        <span v-if="post.adoptedCommentId">&#9989; &nbsp &nbsp</span>
        <span class="fw-bold">[{{ post.category }}] {{ post.title }}</span>
      </div>
      <div class="col-sm-3 text-end">
        <span class>조회수: {{ post.hits }}</span>
      </div>
    </div>

    <hr class="mb-4">

    <div class="row mb-4">
      <div class="col">
        <div class="min-vh-50">
          <textarea class="form-control-plaintext mb-3" rows="15" readonly>{{ post.content }}</textarea>
        </div>
      </div>
    </div>
    <hr class="row mb-4">

    <div class="row mb-3">
      <div class="col-sm-3">
        <span class="fw-bold">첨부파일</span>
      </div>
      <div class="col-sm-9">
        <div v-for="file in fileList" :key="file.fileName">
          <a href="#" @click="downloadFile(file)">{{ file.fileRealName }}</a>
        </div>
      </div>
    </div>

    <hr class="mb-4">

    <Comment :current-user-info="currentUserInfo" :comment-list="commentList" :author-of-post="authorOfPost" :adoptedCommentId="post.adoptedCommentId"
             @addComment="addComment" @modifyComment="modifyComment" @deleteComment="deleteComment" @adoptComment="adoptComment">
      <template v-slot:show-adoption="{ comment, adoptedCommentId }">
        <span v-if="comment.commentId === adoptedCommentId">&#9989;&nbsp;</span>
      </template>
      <template v-slot:adoption="{ adoptCommentEvent, comment, adoptedCommentId }">
        <button class="btn btn-outline-dark" v-if="authorOfPost && !adoptedCommentId" @click="adoptCommentEvent(comment)">채택</button>
      </template>
    </Comment>

    <hr class="mb-4">

    <div class="d-flex justify-content-center">
      <button class="btn btn-secondary me-3" @click="backToList">목록</button>
      <button class="btn btn-secondary me-3" v-if="authorOfPost" @click="moveToModifyView">수정</button>
      <button class="btn btn-secondary" v-if="authorOfPost" @click="deletePost">삭제</button>
    </div>

    <hr class="mt-4">
  </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import Comment from "@/components/Comment.vue";
import NavBar from "@/components/NavBar.vue";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import * as boardApi from "@/apis/board";
import * as userApi from "@/apis/user";
import router from "@/router/router";
import {convertCommentListDataFormat, convertPostFormat} from "@/utils/format-converter";
import {apiErrorHandler} from "@/error/api-error-handler";

onMounted(() => {
  getPost()
})

const route = useRoute()
const store = useStore()

const post = ref({})
const fileList = ref({})
const commentList = ref({})
const authorOfPost = ref(false)
const currentUserInfo = ref('')

/**
 * 게시글 요청 및 게시글 저자 여부 플래그 설정
 * @returns post
 */
const getPost = async () => {
  try {
    const response = await boardApi.getPost(`boards/qna/${route.params.postId}`)

    post.value = convertPostFormat(response.data.post)
    fileList.value = response.data.fileList
    commentList.value = convertCommentListDataFormat(response.data.commentList)

    // 수정 삭제 버튼을 보여주기 위한 flag 변수 처리
    if (store.getters.isValidToken) {
      const authorityResponse = await userApi.getMyInfo()

      currentUserInfo.value = authorityResponse.data
      if (authorityResponse.data.userId === post.value.authorId) {
        authorOfPost.value = true
      }
    }

  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 게시글 삭제 메소드
 */
const deletePost = async () => {
  try {
    const response = await boardApi.deletePost(`boards/qna/${post.value.postId}`)
    alert("게시글을 삭제하는 데 성공했습니다.")
    router.back()
  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 요청한 파일에 대해 다운로드를 수행하는 메소드
 * @param file
 * @returns 다운로드를 요청한 파일
 */
const downloadFile = async (file) => {
  try {
    const response = await boardApi.downloadFile('boards/qna/file', file)
    const blob = new Blob([response.data], {type: response.headers['content-type']})
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')

    link.href = url
    link.download = file.fileRealName
    document.body.appendChild(link)
    link.click()

  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 댓글 작성 메소드
 * @param comment
 */
const addComment = async (comment) => {
  const data = {
    postId: post.value.postId,
    comment: comment.value
  }

  try {
    const response = await boardApi.addComment('boards/qna/comment', data)
    alert("댓글을 성공적으로 등록했습니다.")
    router.go(0)
  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 댓글 수정 메소드
 */
const modifyComment = async (comment) => {
  console.log("modifyComment: " + JSON.stringify(comment))
  try {
    const response = await boardApi.modifyComment('boards/qna/comment', comment)
    alert("댓글을 성공적으로 수정했습니다.")
    router.go(0)
  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 댓글 삭제 메소드
 * @param comment
 */
const deleteComment = async (comment) => {
  try {
    const response = await boardApi.deleteComment(`boards/qna/comment/${comment.commentId}`)
    alert("댓글을 성공적으로 삭제했습니다")
    router.go(0)
  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 댓글 채택 메소드
 *
 */
const adoptComment = async (comment) => {
  console.log(`adoptcommnet: ${comment.commentId}`)
  const data = {
    postId: post.value.postId,
    adoptedCommentId: comment.commentId
  }
  try {
    const response = await boardApi.adoptComment(`boards/qna/${post.value.postId}/adoption`, data)
    alert('채택 완료')
    router.go(0)
  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 이전 페이지(목록)으로 이동
 */
const backToList = () => {
  router.back()
}

/**
 * 게시글 수정 페이지로 이동
 */
const moveToModifyView = () => {
  console.log("route path: " + route.path + '/edit')
  router.push({name: 'QnAModifyView'})
}
</script>

<style scoped>
</style>