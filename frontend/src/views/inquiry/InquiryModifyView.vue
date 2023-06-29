<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>문의 - 수정</h1>
    <br>
    <div class="row mb-3">
      <label for="title" class="col-sm-2 col-form-label">제목</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" id="title" v-model="post.title" name="title"
               required>
      </div>
    </div>
    <div class="row mb-3">
      <label for="content" class="col-sm-2 col-form-label">내용</label>
      <div class="col-sm-10">
        <textarea class="form-control" id="content" v-model="post.content" name="content" rows="20"
                  placeholder="내용을 입력하세요"
                  required></textarea>
      </div>
    </div>
    <div class="row mb-3">
      <label class="col-sm-2 col-form-label">파일 첨부</label>
      <div class="col-sm-10">
        <div class="mb-3" v-for="(n, i) in 3">
          <input type="file" class="form-control" v-if="!existingFileList[i]" @change="addFile(0, $event)">
          <div v-if="existingFileList[i]" class="input-group">
            <span class="input-group-text">{{ existingFileList[i].fileRealName }}</span>
            <button type="button" class="btn btn-danger" @click="deleteFile(i)">삭제</button>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-10 offset-sm-2">
        <button class="btn btn-primary" @click="modifyPost">저장</button>
        <button class="btn btn-secondary me-2" @click="$router.back()">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import NavBar from "@/components/NavBar.vue"
import router from "@/router/router";
import {defineComponent, onBeforeMount, onMounted, ref} from "vue";
import * as boardApi from "@/apis/board"
import * as userApi from "@/apis/user"
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import {apiErrorHanlder} from "@/error/api-error-hanlder";


const route = useRoute()
const store = useStore()

const post = ref({})
const fileList = ref([])
const commentList = ref({})
const categoryList = ref({})
const existingFileList = ref({})

onMounted(() => {
  getPost()
})

/**
 * 수정 화면에서 사용할 게시글 정보
 * @returns post
 */
const getPost = async () => {
  try {
    const response = await boardApi.getPost(`boards/inquiry/${route.params.postId}`)

    // 응답 데이터 추가
    post.value = response.data.post
    existingFileList.value = response.data.fileList
    commentList.value = response.data.commentList

  } catch (error) {
    apiErrorHanlder(error)
  }
}

/**
 * 게시글 수정
 */
const modifyPost = async () => {
  const formData = new FormData()

  // 게시글 데이터 입력
  for (const key in post.value) {
    if (post.value[key]) {
      formData.append(key, post.value[key])
    }
  }

  // 신규 파일 입력
  for (let i = 0; i < fileList.value.length; i++) {
    if (fileList.value[i]) {
      console.log("i: " + i + ", 입력")
      formData.append("file", fileList.value[i])
    }
  }

  // 기존 파일 리스트 입력
  const json = JSON.stringify(existingFileList.value)
  const blob = new Blob([json], { type: "application/json"})
  formData.append("existingFiles", blob)

  console.log("existingfileList: "  + existingFileList.value)

  try {
    const response = await boardApi.modifyPost(`boards/inquiry/${post.value.postId}`, formData)

    alert('게시글을 성공적으로 수정했습니다.')
    router.back()
  } catch (error) {
    apiErrorHanlder(error)
  }
}

/**
 * 파일 추가 메소드
 * @param number 작업이 이뤄지는 파일 인덱스
 * @param event 입력한 파일이 존재하는 이벤트 객체
 */
const addFile = (number, event) => {
  const files = event.target.files

  // 입력한 파일이 존재하면 file 객체
  fileList.value[number] = files[0] || null
}

/**
 * 파일 삭제 메소드
 * @param number 파일 인덱스
 */
const deleteFile = (number) => {
  if (existingFileList.value[number]) {
    console.log("existingfile 제거")
    existingFileList.value[number] = null
  }
}

</script>

<style scoped>

</style>