<template>
  <nav class="nav me-5 navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <span class="d-inline-block text-truncate">
      <router-link class="nav-link text-white" to="/">메인 페이지</router-link>
    </span>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse container-fluid" id="navbarNav">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link class="nav-link" to="/boards/notice">공지사항</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/boards/qna">Q&A</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/boards/free">커뮤니티</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/boards/image">이미지 갤러리</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/boards/inquiry">1:1문의</router-link>
        </li>
      </ul>
      <ul class="navbar-nav justify-content-end">
        <li class="nav-item">
          <button v-if="!$store.getters.isValidToken" type="button" class="btn btn-primary" @click="showModal = true">
            로그인
          </button>
          <button v-else type="button" class="btn btn-primary" @click="signout">
            로그아웃
          </button>
        </li>
      </ul>
    </div>
  </nav>

  <div>
    <!-- 로그인 모달 -->
    <div class="modal" tabindex="-1" role="dialog" :class="{ 'd-block': showModal }">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">로그인</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close" @click="showModal = false">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <!-- 로그인 폼 -->
            <form>
              <div class="form-group">
                <label for="username">아이디</label>
                <input type="text" class="form-control" id="username" v-model="loginId" />
              </div>
              <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" class="form-control" id="password" v-model="password" />
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" @click="signin">로그인</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal" @click="showModal = false">
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {useRouter} from "vue-router";
import {useStore} from "vuex";
import {ref} from "vue";
import axios from "axios";

export default {
  name: "NavBar",

  setup() {
    const router = useRouter()
    const store = useStore()

    const showModal = ref(false)
    const loginId = ref()
    const password = ref()

    /**
     * 로그인 수행 메소드
     * @returns {Promise<void>}
     */
    const signin = async() => {
      try {
        const response = await axios.post("/api/signin", {
          loginId: loginId.value,
          password: password.value
        })
        console.log(response.data.token)
        await store.dispatch('setToken', response.data.token)
        alert("로그인에 성공했습니다.")

        location.reload()
      } catch (e) {
        alert("아이디 혹은 패스워드가 잘못되었습니다.")
      }
    }

    const signout = async () => {
      console.log(store.getters.isValidToken)
      if (store.getters.isValidToken) {
        await store.dispatch('removeToken')
        alert("로그아웃에 성공했습니다.")
        location.reload()
      }
    }

    return {
      showModal,
      loginId,
      password,
      signin,
      signout
    }
  }
}
</script>

<style scoped>

</style>