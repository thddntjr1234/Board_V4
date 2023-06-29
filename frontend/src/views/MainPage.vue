<template>
  <NavBar></NavBar>
  <div class="container">
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
      </div>
    </div>
  </div>
</template>

<script setup>
import NavBar from "@/components/NavBar.vue";
</script>

<style scoped>
</style>