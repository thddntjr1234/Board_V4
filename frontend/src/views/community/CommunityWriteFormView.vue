<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>커뮤니티 - 작성</h1>
    <br>
    <div class="row mb-3">
      <label for="categoryId" class="col-sm-2 col-form-label">카테고리</label>
      <div class="col-sm-10">
        <select id="categoryId" class="form-select" name="categoryId" v-model="categoryId" required>
          <option disabled value="">카테고리 선택</option>
          <option v-for="category in categoryList" :key="category" :value="category.categoryId">
            {{ category.category }}
          </option>
        </select>
      </div>
    </div>
    <div class="row mb-3">
      <label for="title" class="col-sm-2 col-form-label">제목</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" id="title" v-model="title" name="title" placeholder="제목을 입력하세요"
               required>
      </div>
    </div>
    <div class="row mb-3">
      <label for="content" class="col-sm-2 col-form-label">내용</label>
      <div class="col-sm-10">
        <textarea class="form-control" id="content" v-model="content" name="content" rows="20" placeholder="내용을 입력하세요"
                  required></textarea>
      </div>
    </div>
    <div class="row mb-3">
      <label class="col-sm-2 col-form-label">파일 첨부</label>
      <div class="col-sm-10">
        <div class="mb-3" v-for="(n, i) in 3">
          <input type="file" class="form-control" @change="addFile(i, $event)">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-10 offset-sm-2">
        <button class="btn btn-secondary me-2" @click="$router.push({ name: 'CommunityBoardView' })">목록</button>
        <button class="btn btn-primary" @click="savePost">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useStore} from "vuex"
import {apiErrorHandler} from "@/error/api-error-handler";
import {validateFormData} from "@/utils/validation";
import NavBar from "@/components/NavBar.vue";
import * as boardApi from "@/apis/board"
import {useRouter} from "vue-router";

const store = useStore()

const categoryList = ref()
const router = useRouter()

// 변수를 ref 혹은 reactive로 감싸면 반응형으로 바뀐다.
const categoryId = ref('')
const title = ref('')
const content = ref('')
const file = ref([])
const post = ref({})


onMounted(() => {
  getWriteFormData()
})

/**
 * 입력한 게시글 데이터를 서버 상으로 저장하도록 요청하는 메소드
 */
const savePost = async () => {
  const formData = new FormData()

  // ref에 접근하려면 .value옵션을 붙여야 함
  formData.append("title", title.value)
  formData.append("content", content.value)
  formData.append("categoryId", categoryId.value)

  if (!validateFormData(formData)) {
    return
  }

  // 파일이 있다면 이를 일일히 append해야 리스트 단위로 들어가지 않는다.
  for (let i = 0; i < file.value.length; i++) {
    if (file.value[i]) {
      formData.append("file", file.value[i])
    }
  }

  try {
    const response = await boardApi.savePost('/boards/free', formData)
    alert("게시글 저장 성공")
    router.push({name: 'CommunityBoardView'})

  } catch (error) {
    apiErrorHandler(error)
  }
}

/**
 * 게시글 폼에 바인딩할 데이터 요청
 * @returns categoryList, user(info)
 */
const getWriteFormData = async () => {
  try {
    const response = await boardApi.getWriteFormData("boards/free/new")
    categoryList.value = response.data.categoryList

  } catch (error) {
    apiErrorHandler(error)
    router.push({name: 'CommunityBoardView'})
  }
}

// 파일 추가 시 file 리스트에 추가
const addFile = (number, event) => {
  const files = event.target.files

  // 입력한 파일이 존재하지 않으면 null값 입력
  file.value[number] = files[0] || null
}

</script>

<style scoped>

</style>