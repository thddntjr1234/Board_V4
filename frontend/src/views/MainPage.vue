<template>
  <div class="container">
    <NavBar></NavBar>
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card mb-3">
          <div class="card-body">
            <h5 class="card-title">
              <router-link to="/boards/free">커뮤니티</router-link>
            </h5>
            <p class="card-text">게시글 내용</p>
            <p class="card-text"><small class="text-muted">게시글 작성일</small></p>
          </div>
        </div>
        <!-- 게시글 목록 더 추가 가능 -->
      </div>
      <div class="col-md-6">
        <div class="card mb-3">
          <div class="card-body">
            <h5 class="card-title">
              <router-link to="/board/qna">Q&A</router-link>
            </h5>
            <ul class="list-group">
              <li class="list-group-item"><a href="#">게시글 제목</a></li>
              <!-- 최신 게시글 더 추가 가능 -->
            </ul>
          </div>
        </div>
        <div class="card mb-3">
          <div class="card-body">
            <h5 class="card-title">인기 게시글</h5>
            <ul class="list-group">
              <li class="list-group-item"><a href="#">게시글 제목</a></li>
              <!-- 인기 게시글 더 추가 가능 -->
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

// script setup 한번 알아보기
<script>
import {onMounted} from "vue";
import {ref, reactive} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {useStore} from "vuex"
import NavBar from "@/components/NavBar.vue";

export default {
  name: "MainPage",
  components: {NavBar},
  setup() {
  }
}
</script>

<style scoped>

</style>