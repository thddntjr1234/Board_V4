<template>
  <NavBar></NavBar>
  <div class="container-fluid">
    <h1 class="mt-4 justify-content-start">게시판 - 보기</h1>
    <div class="row mb-3">
      <div class="col-sm-3">
        <span class="fw-bold">{{ post.author }}</span>
      </div>
      <div class="col-sm-9 text-end">
        <span class="ms-4">등록일시: {{ post.createdDate }}</span>
        <span class="ms-4" v-if="post.modifiedDate !== null">수정일시: {{ post.modifiedDate }}</span>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col-sm-3">
        <span class="fw-bold">[{{ post.category }}] {{ post.title }}</span>
      </div>
      <div class="col-sm-3">

      </div>
      <div class="col-sm-6 text-end">
        <span class>조회수: {{ post.hits }}</span>
      </div>
    </div>

    <hr class="mb-4">

    <div class="row mb-4">
      <div class="col">
        <div class="min-vh-50">
          <p class="form-control mb-3" rowspan="20">{{ post.content }}</p>
        </div>
      </div>
    </div>

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

    <Comment :current-user-info="currentUserInfo" :comment-list="commentList"
             @addComment="addComment" @modifyComment="modifyComment" @deleteComment="deleteComment"></Comment>

    <hr class="mb-4">

    <div class="d-flex justify-content-center">
      <button class="btn btn-secondary me-3" @click="backToList">목록</button>
      <button class="btn btn-secondary me-3" v-if="authorOfPost" @click="modifyPost">수정</button>
      <button class="btn btn-secondary" v-if="authorOfPost" @click="deletePost">삭제</button>
    </div>

    <hr class="mt-4">
  </div>
</template>

<script>
import {onBeforeMount, onMounted, ref, reactive} from "vue";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import router from "@/router/router";
import NavBar from "@/components/NavBar.vue";
import Comment from "@/components/Comment.vue"
import * as boardApi from "@/apis/board"
import * as userApi from "@/apis/user"

export default {
  name: "CommunityView",
  components: {NavBar, Comment},

  setup() {
    onMounted(() => {
      getPost()
    })

    const route = useRoute()
    const store = useStore()

    const post = ref({
      postId: null,
      hits: null,
      categoryId: null,
      createdDate: null,
      modifiedDate: null,
      title: null,
      content: null,
      authorId: null,
      author: null,
      category: '',
      passwd: null,
      confirmPasswd: null,
      fileFlag: false,
      file: null
    })
    const fileList = ref({})
    const commentList = ref({})
    const authorOfPost = ref(false)
    const currentUserInfo = ref('')
    const newComment = ref('')

    /**
     * 게시글 요청 및 게시글 저자 여부 플래그 설정
     * @returns post
     */
    const getPost = async () => {
      try {
        const response = await boardApi.getPost(`boards/free/${route.params.postId}`)

        post.value = response.data.data.post
        fileList.value = response.data.data.fileList
        commentList.value = response.data.data.commentList

        // 수정 삭제 버튼을 보여주기 위한 flag 변수 처리
        if (store.getters.isValidToken) {
          const authorityResponse = await userApi.getMyInfo()

          currentUserInfo.value = authorityResponse.data
          if (authorityResponse.data.userId === post.value.authorId) {
            authorOfPost.value = true
          }
        }

      } catch (error) {
        await router.push({name: 'not-found'})
        console.error("게시글 데이터를 받아오는 데 실패했습니다")
      }
    }

    /**
     * 게시글 삭제 메소드
     */
    const deletePost = async () => {
      try {
        const response = await boardApi.deleteComment(`boards/free/${post.value.postId}`)
        alert("게시글을 삭제하는 데 성공했습니다.")
        router.back()
      } catch (error) {
        alert("게시글을 삭제하는 데 실패했습니다.")
      }
    }

    /**
     * 요청한 파일에 대해 다운로드를 수행하는 메소드
     * @param file
     * @returns 다운로드를 요청한 파일
     */
    const downloadFile = async (file) => {
      try {
        const response = await boardApi.downloadFile('boards/free/file', file)
        const blob = new Blob([response.data], {type: response.headers['content-type']})
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')

        link.href = url
        link.download = file.fileRealName
        document.body.appendChild(link)
        link.click()

      } catch (error) {
        alert("파일 다운로드에 실패했습니다.")
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
        const response = await boardApi.addComment('boards/free/comment', data)
        alert("댓글을 성공적으로 등록했습니다.")
        router.go(0)
      } catch (error) {
        alert("댓글을 등록하는 데 실패했습니다.")
      }
    }

    /**
     * 댓글 수정 메소드
     */
    const modifyComment = async (comment) => {
      console.log("modifyComment: " + JSON.stringify(comment))
      try {
        const response = await boardApi.modifyComment('boards/free/comment', comment)
        alert("댓글을 성공적으로 수정했습니다.")
        router.go(0)
      } catch (error) {
        alert("댓글 수정하는 데 실패했습니다.")
      }
    }

    /**
     * 댓글 삭제 메소드
     * @param comment
     */
    const deleteComment = async (comment) => {
      try {
        const response = await boardApi.deleteComment(`boards/free/comment/${comment.commentId}`)
        alert("댓글을 성공적으로 삭제했습니다")
        router.go(0)
      } catch (error) {
        alert("댓글을 삭제하는 데 실패했습니다.")
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
      router.push(route.path + '/edit')
    }


    return {
      post,
      fileList,
      commentList,
      authorOfPost,
      currentUserInfo,
      newComment,
      backToList,
      modifyPost: moveToModifyView,
      deletePost,
      addComment,
      modifyComment,
      deleteComment,
      downloadFile
    }
  }
}


</script>

<style scoped>

</style>