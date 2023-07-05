<template>
  <NavBar></NavBar>
  <div class="container">
    <h1 class="mt-4 justify-content-start">이미지 갤러리 - 게시글</h1>
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
        <span class="fw-bold">{{ post.title }}</span>
      </div>
      <div class="col-sm-3 text-end">
        <span class>조회수: {{ post.hits }}</span>
      </div>
    </div>

    <hr class="mb-4">

    <div class="row mb-4">
      <div id="viewer" class="col-sm text-start">
<!--        <div v-html="post.content"></div>-->
      </div>
    </div>
    <hr class="row mb-4">

    <hr class="mb-4">

    <Comment :current-user-info="currentUserInfo" :comment-list="commentList"
             @addComment="addComment" @modifyComment="modifyComment" @deleteComment="deleteComment"></Comment>

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
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import * as boardApi from "@/apis/board";
import * as userApi from "@/apis/user";
import router from "@/router/router";
import NavBar from "@/components/NavBar.vue";
import Comment from "@/components/Comment.vue";
import {convertCommentListDataFormat, convertPostFormat} from "@/utils/format-converter";
import {Editor} from "@toast-ui/editor"
import {apiErrorHandler} from "@/error/api-error-handler";

onMounted(async () => {
  await getPost()
  setViewer()
})

const route = useRoute()
const store = useStore()

const post = ref({})
const fileList = ref({})
const commentList = ref({})
const authorOfPost = ref(false)
const currentUserInfo = ref('')
const viewer = ref()

/**
 * 게시글 요청 및 게시글 저자 여부 플래그 설정
 * @returns post
 */
const getPost = async () => {
  try {
    const response = await boardApi.getPost(`boards/gallery/${route.params.postId}`)

    post.value = convertPostFormat(response.data.post)
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
    apiErrorHandler()
  }
}

/**
 * Toast UI Editor에 내장된 뷰어를 설정
 */
const setViewer = () => {
  viewer.value = Editor.factory({
    el: document.querySelector('#viewer'),
    viewer: true,
    height: '600px',
    initialValue: post.value.content
  })
}

/**
 * 게시글 삭제 메소드
 */
const deletePost = async () => {
  try {
    const response = await boardApi.deletePost(`boards/gallery/${post.value.postId}`)
    alert("게시글을 삭제하는 데 성공했습니다.")
    router.back()
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
    const response = await boardApi.addComment('boards/gallery/comment', data)
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
  // console.log("modifyComment: " + JSON.stringify(comment))

  // 전달된 파라미터로 얕은 복사를 수행해 replace값이 수정 버튼을 눌렀을 때 노출되지 않도록 한다.
  const data = { ...comment }

  data.comment = data.comment.replace(/\n/g, '<br>')
  try {
    const response = await boardApi.modifyComment('boards/gallery/comment', data)
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
    const response = await boardApi.deleteComment(`boards/gallery/comment/${comment.commentId}`)
    alert("댓글을 성공적으로 삭제했습니다")
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
  router.push({name: "GalleryModifyView"})
}
</script>

<style scoped>

</style>