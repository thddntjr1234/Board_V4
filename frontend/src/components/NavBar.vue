<template>
  <div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div class="d-flex justify-content-between align-items-center w-100 mx-5">
      <span class="d-inline-block text-truncate me-4">
        <router-link class="nav-link text-white" to="/">메인 페이지</router-link>
      </span>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav flex-fill">
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
              <router-link class="nav-link" to="/boards/gallery">이미지 갤러리</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/boards/inquiry">1:1문의</router-link>
            </li>
          </ul>
          <ul class="navbar-nav">
            <li class="nav-item">
              <button v-if="!$store.getters.isValidToken" type="button" class="btn btn-primary"
                      @click="showModal = true">
                로그인
              </button>
              <button v-else type="button" class="btn btn-primary" @click="signout">
                로그아웃
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div>
      <!-- 로그인 모달 -->
      <div class="modal" tabindex="-1" role="dialog" :class="{ 'd-block': showModal }">
        <div class="modal-dialog modal-dialog-centered position-absolute" role="document"
             style="top: 50%; left: 50%; transform: translate(-50%, -50%);">
          <!--        <div class="modal-dialog" role="document">-->
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
                  <input type="text" class="form-control" id="username" v-model="loginId"/>
                </div>
                <div class="form-group">
                  <label for="password">비밀번호</label>
                  <input type="password" class="form-control" id="password" v-model="password"/>
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
  </div>
</template>

<script>
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import {ref} from "vue";
import * as userApi from "@/apis/user"

export default {
  name: "NavBar",

  setup() {
    const route = useRoute()
    const store = useStore()

    const showModal = ref(false)
    const loginId = ref()
    const password = ref()

    /**
     * 로그인 수행 메소드
     * @returns {Promise<void>}
     */
    const signin = async () => {
      const data = {
        loginId: loginId.value,
        password: password.value
      }

      try {
        const response = await userApi.signin(data)
        console.log(response.data.token)
        await store.dispatch('setToken', response.data.token)
        alert("로그인에 성공했습니다.")
        location.reload()

      } catch (e) {
        alert("아이디 혹은 패스워드가 잘못되었습니다.")
      }
    }

    /**
     * 로그아웃시 토큰을 vuex 스토리지에서 제거
     * @returns {Promise<void>}
     */
    const signout = async () => {
      console.log(store.getters.isValidToken)
      if (store.getters.isValidToken) {
        await store.dispatch('removeToken')
        alert("로그아웃에 성공했습니다.")

        // 로그아웃 시 쿼리 파라미터를 제거한 경로로 리다이렉트
        const path = route.path.split('?')[0]
        location.replace(path)
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