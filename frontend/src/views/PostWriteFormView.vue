<template>
  <body>
  <div class="container">
    <h1>게시판 - 등록</h1>
  </div>
  <br>
  <div class="container">
    <table class="table">
      <tr>
        <td colspan="2">카테고리</td>
        <td>
          <select class="form-select-sm" name="categoryId" v-model="categoryId" required>
            <option disabled value="">카테고리 선택</option>
            <option v-for="category in categoryList" :key="category" :value="category.categoryId">
              {{ category.category }}
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <td colspan="2">작성자</td>
        <td><input class="form-text" v-model="author" name="author" required></td>
      </tr>
      <tr>
        <td colspan="2">비밀번호</td>
        <td>
          <input type="password" class="form-control-sm" v-model="passwd" placeholder="비밀번호"
                 required>&nbsp
          <input type="password" class="form-control-sm" v-model="confirmPasswd"
                 placeholder="비밀번호 확인" required>
        </td>
      </tr>
      <tr>
        <td colspan="2">제목</td>
        <td><input class="form-text" v-model="title" name="title" size="100" required></td>
      </tr>
      <tr>
        <td colspan="2">내용</td>
        <td><textarea class="form-text" v-model="content" name="content" cols="100" rows="20"
                      required></textarea></td>
      </tr>
      <tr>
        <td colspan="2" rowspan="3">파일 첨부</td>
        <td>
          <div class="form-control"><input type="file" @change="addFile" name="file"></div>
          <div class="form-control"><input type="file" @change="addFile" name="file"></div>
          <div class="form-control"><input type="file" @change="addFile" name="file"></div>
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <button class="btn btn-secondary">
            <router-link :to="{name: 'board'}">목록</router-link>
          </button>
        </td>
        <td>
          <button class="btn btn-secondary" @click="savePost">저장</button>
        </td>
      </tr>
    </table>
  </div>
  </body>
</template>

<script>
import {onMounted} from "vue";
import {ref, reactive} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  name: "PostWriteForm",


  setup() {
    const router = useRouter()

    const categoryList = ref()

    // 변수를 ref 혹은 reactive로 감싸면 반응형으로 바뀐다.
    const categoryId = ref()
    const title = ref()
    const content = ref()
    const author = ref()
    const createdDate = ref()
    const passwd = ref()
    const confirmPasswd = ref()
    const file = ref([])

    // 파일을 전송하기 위해서는 FormData가 필요하다
    const savePost = async () => {
      const formData = new FormData()

      // ref에 접근하려면 .value옵션을 붙여야 함
      formData.append("categoryId", categoryId.value)
      formData.append("title", title.value)
      formData.append("content", content.value)
      formData.append("author", author.value)
      formData.append("createdDate", createdDate.value)
      formData.append("passwd", passwd.value)
      formData.append("confirmPasswd", confirmPasswd.value)

      // 왜 [Object FileList로 들어가나
      for (let i = 0; i < file.value.length; i++) {
        formData.append("file", file.value[i])
      }

      try {
        alert("formData: " + formData)
        const response = await axios.post("/api/boards/free", formData, {
          headers: {'Content-Type': 'multipart/form-data'}
        })
        alert("게시글 저장 성공" + response.data.success)

      } catch (e) { // 에러 처리는 나중에, 일단 콘솔에 표시만
        alert("게시글 저장에 실패했습니다. 에러: " + e)
      }

      await router.push({name: 'board'})
    }

    // 카테고리 리스트를 요청
    const getCategoryList = async () => {
      const response = await axios.get("/api/boards/free/new")
      console.log("받아온 카테고리 리스트 내용: " + JSON.stringify(response.data.data))
      categoryList.value = response.data.data
    }

    // 파일 추가 시 file 리스트에 추가
    // 파일 등록후 다시 등록 버튼을 누른 뒤 취소하면 파일이 지워지는데, 반영되지 않는 오류가 있다.
    const addFile = (event) => {
      const files = event.target.files
      for (let i = 0; i < files.length; i++) {
        file.value.push(files[i])
      }
    }

    onMounted(() => {
      getCategoryList()
    })

    return {
      categoryList,
      categoryId,
      title,
      content,
      author,
      createdDate,
      passwd,
      confirmPasswd,
      file,
      addFile,
      savePost
    }
  }
}
</script>

<style scoped>

</style>