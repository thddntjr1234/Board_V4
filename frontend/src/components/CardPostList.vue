<template>
  <div class="container">
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-2 g-4">
      <div v-for="post in postList" :key="post" class="col">
        <div class="card">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <h6 class="m-0">{{ post.author }}</h6>
              <div class="d-flex align-items-end">
                <p class="m-0">{{ post.createdDate }}</p>
                <p v-if="post.modifiedDate" class="m-0">수정됨</p>
              </div>
            </div>
            <div class="d-flex justify-content-start">
              <div v-if="showQnA">
                <p class="m-0" v-if="post.adoptedCommentId">&#9989;</p>
              </div>
              <h5 class="card-title">
                <router-link :to="{path: `/boards/${boardName}/${post.postId}`}">{{ post.title }}</router-link>
              </h5>
            </div>
            <div class="d-flex justify-content-between align-items-center">
              <p class="m-0">{{ post.category }}</p>
              <div class="d-flex align-items-end">
                <p class="m-0">조회수: {{ post.hits }}</p>
              </div>
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
  showQnA: false
})
</script>

<style scoped>

</style>