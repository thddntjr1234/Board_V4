<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>공지사항 - 수정</h1>
    <br>
    <div class="row mb-3">
      <label for="categoryId" class="col-sm-2 col-form-label">카테고리</label>
      <div class="col-sm-10">
        <select id="categoryId" class="form-select" name="categoryId" v-model="post.target" required>
          <option disabled value="">공지사항 노출 대상</option>
          <option value="all">전체 게시판</option>
          <option value="community">커뮤니티 게시판</option>
          <option value="qna">Q&A 게시판</option>
          <option value="inquiry">1:1문의 게시판</option>
          <option value="gallery">이미지 게시판</option>
          <option value="none">공지사항 게시판에만 노출</option>
        </select>
      </div>
    </div>
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
import {onMounted, ref} from "vue";
import * as boardApi from "@/apis/board"
import * as userApi from "@/apis/user"
import {useRoute} from "vue-router";
import {useStore} from "vuex";


const route = useRoute()
const store = useStore()

const post = ref({})
const fileList = ref([])
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
    const response = await boardApi.getPost(`boards/notice/${route.params.postId}`)

    // 응답 데이터 추가
    post.value = response.data.data.post
    existingFileList.value = response.data.data.fileList

  } catch (error) {
    await router.push({name: 'not-found'})
    console.error("게시글 데이터를 받아오는 데 실패했습니다")
  }
}

/**
 * 게시글 폼에 바인딩할 데이터 요청
 * @returns categoryList, user(info)
 */
const getCategoryList = async () => {
  try {
    const response = await boardApi.getCategoryList('boards/notice/new')

  } catch (error) {
    console.error("비회원 접근, 이전 페이지로 리다이렉트한다.")
    alert("게시글 작성은 회원만 가능합니다.")
    await router.push({name: 'CommunityBoardView'})
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
    const response = await boardApi.modifyPost(`boards/notice/${post.value.postId}`, formData)

    alert('게시글을 성공적으로 수정했습니다.')
    router.back()
  } catch (error) {
    alert('게시글을 수정하는 데 실패했습니다.')
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
    existingFileList.value[number] = null
  }
}

</script>

<style scoped>

</style>