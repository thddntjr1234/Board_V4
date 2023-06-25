<template>
  <div class="container">
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-1 g-4">
      <!--상단 공지사항-->
      <div v-for="notice in noticeList" :key="notice" class="col">
        <div class="card bg-primary bg-opacity-10">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <h6 class="m-0">{{ notice.author }}</h6>
              <div class="d-flex align-items-end">
                <p class="m-0">{{ notice.createdDate }}</p>
              </div>
            </div>
            <div class="d-flex justify-content-start">
              <h6 class="card-title">
                <router-link :to="{path: `/boards/notice/${notice.postId}`}">{{ notice.title }}</router-link>
              </h6>
            </div>
            <div class="d-flex justify-content-between align-items-center">
              <p class="m-0">공지사항</p>
              <div class="d-flex align-items-end">
                <p class="m-0" style="padding-left: 10px">조회수: {{ notice.hits }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!--게시글-->
      <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
        <div v-for="post in postList" :key="post" class="col">
          <!--        이미지 갤러리 HTML-->
          <div v-if="boardName === 'gallery'" class="card h-100 border-0">
            <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
              <img v-if="post.thumbnailPath" :src="post.thumbnailPath" alt="이미지" class="card-img-top" style="width: 200px; height: 200px;">
              <img v-else src="/no_thumb.png" alt="이미지" class="card-img-top opacity-25" style="width: 200px; height: 200px;">
            </router-link>
            <div class="card-body">
              <h6 class="card-title mt-0">
                <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">{{ post.title }}</router-link>
              </h6>
              <p class="card-text mb-0">{{ post.author }}</p>
              <p class="card-text mb-0">
                <small class="text-muted">
                  {{ post.createdDate }} 조회수: {{ post.hits }}
                </small>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  boardName: '',
  postList: [],
  noticeList: [],
})
</script>

<style scoped>
a, u {
  text-decoration: none;
}

a:hover, u:hover {
  text-decoration: underline;
}
</style>