<template>
  <NavBar></NavBar>
  <br>
  <div class="container">
    <div class="row">
      <!-- 첫 번째 행 -->
      <div class="col-xl-6">
        <h5>
          <router-link to="/boards/free">커뮤니티</router-link>
        </h5>
        <!-- 게시판 카드 리스트 1 -->
        <div v-for="post in communityPostList.slice(0, 5)" :key="post" class="col">
          <div class="row align-items-center">
            <div class="col">
              <div class="overflow-hidden flex-nowrap">
                <div class="d-flex justify-content-between">
                  <p class="m-0 mb-1">{{ post.author }}</p>
                  <p class="m-0 mb-1">{{ post.createdDate }}</p>
                </div>
                <h6 class="m-0 mb-1 text-start">
                  <router-link :to="{path: `/boards/free/${post.postId}`}" class="text-reset">
                    {{ post.title }} {{ (post.fileFlag) ? '&#128193' : null }}
                  </router-link>
                </h6>
              </div>
            </div>
          </div>
          <!-- 가로선 -->
          <hr v-if="communityPostList.slice(0, 5).indexOf(post) !== communityPostList.slice(0, 5).length - 1"
              class="my-2">
        </div>
      </div>


      <div class="col-xl-6">
        <h5>
          <router-link to="/boards/qna">Q&A</router-link>
        </h5>
        <!-- 게시판 카드 리스트 1 -->
        <div v-for="post in qnaPostList.slice(0, 5)" :key="post" class="col">
          <div class="row align-items-center">
            <div class="col">
              <div class="overflow-hidden flex-nowrap">
                <div class="d-flex justify-content-between">
                  <p class="m-0 mb-1">{{ post.author }}</p>
                  <p class="m-0 mb-1">{{ post.createdDate }}</p>
                </div>
                <h6 class="m-0 mb-1 text-start">
                  <router-link :to="{path: `/boards/qna/${post.postId}`}" class="text-reset">
                    {{ post.title }} {{ (post.fileFlag) ? '&#128193' : null }}
                  </router-link>
                </h6>
              </div>
            </div>
          </div>
          <!-- 가로선 -->
          <hr v-if="qnaPostList.slice(0, 5).indexOf(post) !== qnaPostList.slice(0, 5).length - 1" class="my-2">
        </div>
      </div>
    </div>

    <br>
    <br>

    <div class="row">
      <!-- 두 번째 행 -->
      <div class="col-xl-6">
        <h5>
          <router-link to="/boards/gallery">이미지 갤러리</router-link>
        </h5>
        <!-- 게시판 카드 리스트 2 -->
        <div v-for="post in galleryPostList.slice(0, 5)" :key="post" class="col">
          <div class="row align-items-center">
            <div class="col-auto">
              <router-link :to="{ path: `/boards/gallery/${post.postId}` }" class="text-reset">
                <img v-if="post.thumbnailPath" :src="post.thumbnailPath" alt="이미지" class="card-img"
                     style="width: 50px; height: 50px;">
                <img v-else src="/no_thumb.png" alt="이미지" class="card-img opacity-25"
                     style="width: 50px; height: 50px;">
              </router-link>
            </div>
            <div class="col">
              <div class="overflow-hidden flex-nowrap">
                <div class="d-flex justify-content-between">
                  <p class="m-0 mb-1">{{ post.author }}</p>
                  <p class="m-0 mb-1">{{ post.createdDate }}</p>
                </div>
                <h6 class="m-0 mb-1 text-start">
                  <router-link :to="{ path: `/boards/gallery/${post.postId}` }" class="text-reset">{{
                      post.title
                    }}
                  </router-link>
                </h6>
              </div>
            </div>
          </div>
          <!-- 가로선 -->
          <hr v-if="galleryPostList.slice(0, 5).indexOf(post) !== galleryPostList.slice(0, 5).length - 1" class="my-2">
        </div>
      </div>


      <div class="col-xl-6">
        <h5>
          <router-link to="/boards/inquiry">1:1 문의</router-link>
        </h5>
        <!-- 게시판 카드 리스트 2 -->
        <div v-for="post in inquiryPostList.slice(0, 5)" :key="post" class="col">
          <div class="row align-items-center">
            <div class="col">
              <div class="overflow-hidden flex-nowrap">
                <div class="d-flex justify-content-between">
                  <p class="m-0 mb-1">{{ post.author }}</p>
                  <p class="m-0 mb-1">{{ post.createdDate }}</p>
                </div>
                <h6 class="m-0 mb-1 text-start">
                  <router-link :to="{ path: `/boards/inquiry/${post.postId}` }" class="text-reset">
                    {{ (post.secret) ? '&#128274;' : null }} {{ post.title }} {{ (post.fileFlag) ? '&#128193;' : null }}
                  </router-link>
                </h6>
              </div>
            </div>
          </div>
          <!-- 가로선 -->
          <hr v-if="inquiryPostList.slice(0, 5).indexOf(post) !== inquiryPostList.slice(0, 5).length - 1" class="my-2">
        </div>
      </div>
    </div>

    <br>
    <br>

    <div class="row">
      <div class="col-xl-6">
        <h5>
          <router-link to="/boards/notice">공지사항</router-link>
        </h5>
        <!-- 게시판 카드 리스트 1 -->
        <div v-for="post in noticePostList.slice(0, 5)" :key="post" class="col">
          <div class="row align-items-center">
            <div class="col">
              <div class="overflow-hidden flex-nowrap">
                <div class="d-flex justify-content-between">
                  <p class="m-0 mb-1">{{ post.author }}</p>
                  <p class="m-0 mb-1">{{ post.createdDate }}</p>
                </div>
                <h6 class="m-0 mb-1 text-start">
                  <router-link :to="{ path: `/boards/notice/${post.postId}` }" class="text-reset">
                    {{ post.title }} {{ (post.fileFlag) ? '&#128193' : null }}
                  </router-link>
                </h6>
              </div>
            </div>
          </div>
          <!-- 가로선 -->
          <hr v-if="noticePostList.slice(0, 5).indexOf(post) !== noticePostList.slice(0, 5).length - 1" class="my-2">
        </div>
      </div>
    </div>
  </div>

  <br>
  <br>
</template>

<script setup>
import NavBar from "@/components/NavBar.vue";
import {onMounted, ref} from "vue";
import * as boardApi from "@/apis/board";
import {convertPostListFormat} from "@/utils/format-converter";

const communityPostList = ref([])
const qnaPostList = ref([])
const inquiryPostList = ref([])
const galleryPostList = ref([])
const noticePostList = ref([])

onMounted(() => {
  fetchAllPostList();
})

const fetchAllPostList = async () => {
  const communityResponse = await boardApi.getPostList('boards/free');
  const qnaResponse = await boardApi.getPostList('boards/qna');
  const inquiryResponse = await boardApi.getPostList('boards/inquiry');
  const galleryResponse = await boardApi.getPostList('boards/gallery');
  const noticeResponse = await boardApi.getPostList('boards/notice');

  communityPostList.value = convertPostListFormat(communityResponse.data.postList);
  qnaPostList.value = convertPostListFormat(qnaResponse.data.postList);
  inquiryPostList.value = convertPostListFormat(inquiryResponse.data.postList);
  galleryPostList.value = convertPostListFormat(galleryResponse.data.postList);
  noticePostList.value = convertPostListFormat(noticeResponse.data.postList);
}
</script>

<style scoped>
</style>