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
      <div v-for="post in postList" :key="post" class="col">
        <!--자유 게시판 HTML-->
        <div v-if="boardName === 'free'" class="row">
          <div class="col-xl-12">
            <div class="card mb-3 card-body">
              <div class="row align-items-center">
                <div class="col">
                  <div class="overflow-hidden flex-nowrap">
                    <div class="d-flex justify-content-between">
                      <p class="m-0 mb-1">{{ post.author }}</p>
                      <p class="m-0 mb-1">{{ post.createdDate }}</p>
                    </div>
                    <h6 class="m-0 mb-1 text-start">
                      <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
                        {{ post.title }} {{(post.fileFlag) ? '&#128193' : null}}
                      </router-link>
                    </h6>
                    <div class="d-flex justify-content-between">
                      <small class="m-0 mb-1">{{ post.category }}</small>
                      <small class="m-0 mb-1 ">조회수: {{ post.hits }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!--공지사항 게시판 HTML-->
        <div v-if="boardName === 'notice'" class="row">
          <div class="col-xl-12">
            <div class="card mb-3 card-body">
              <div class="row align-items-center">
                <div class="col">
                  <div class="overflow-hidden flex-nowrap">
                    <div class="d-flex justify-content-between">
                      <p class="m-0 mb-1">{{ post.author }}</p>
                      <p class="m-0 mb-1">{{ post.createdDate }}</p>
                    </div>
                    <h5 class="m-0 mb-1 text-start">
                      <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
                        {{ post.title }}  {{ (post.commentCount) ? `[${post.commentCount}]` : null }}
                      </router-link>
                    </h5>
                    <div class="d-flex justify-content-between">
                      <small class="m-0 mb-1">{{ '공지사항' }}</small>
                      <small class="m-0 mb-1 ">조회수: {{ post.hits }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!--Q&A 게시판 HTML-->
        <div v-if="boardName === 'qna'" class="row">
          <div class="col-xl-12">
            <div class="card mb-3 card-body">
              <div class="row align-items-center">
                <div class="col">
                  <div class="overflow-hidden flex-nowrap">
                    <div class="d-flex justify-content-between">
                      <p class="m-0 mb-1">{{ post.author }}</p>
                      <p class="m-0 mb-1">{{ post.createdDate }}</p>
                    </div>
                    <h6 class="m-0 mb-1 text-start">
                      <router-link :to="{path: `/boards/${boardName}/${post.postId}`}">
                        {{ (post.adoptedCommentId) ? '&#9989;' : null }} {{ post.title }} {{(post.fileFlag) ? '&#128193' : null}}
                      </router-link>
                    </h6>
                    <div class="d-flex justify-content-between">
                      <small class="m-0 mb-1">{{ post.category }}</small>
                      <small class="m-0 mb-1 ">조회수: {{ post.hits }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!--문의 게시판 HTML-->
        <div v-if="boardName === 'inquiry'" class="row">
          <div class="col-xl-12">
            <div class="card mb-3 card-body">
              <div class="row align-items-center">
                <div class="col">
                  <div class="overflow-hidden flex-nowrap">
                    <div class="d-flex justify-content-between">
                      <p class="m-0 mb-1">{{ post.author }}</p>
                      <p class="m-0 mb-1">{{ post.createdDate }}</p>
                    </div>
                    <h6 class="m-0 mb-1 text-start">
                      <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
                        {{ (post.secret) ? '&#128274;' : null }} {{ post.title }} {{(post.fileFlag) ? '&#128193' : null}}
                      </router-link>
                    </h6>
                    <div class="d-flex justify-content-between">
                      <small class="m-0 mb-1">{{ (post.answerStatus) ? '[답변 완료]' : '[답변 대기중]' }}</small>
                      <small class="m-0 mb-1 ">조회수: {{ post.hits }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>


        <!--        이미지 갤러리 HTML-->
        <div v-if="boardName === 'gallery'" class="row">
          <div class="col-xl-12">
            <div class="card mb-3 card-body">
              <div class="row align-items-center">
                <div class="col-auto" v-if="boardName === 'gallery'">
                  <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
                    <img v-if="post.thumbnailPath" :src="post.thumbnailPath" alt="이미지" class="card-img" style="width: 90px; height: 90px;">
                    <img v-else src="/no_thumb.png" alt="이미지" class="card-img opacity-25">
                  </router-link>
                </div>
                <div class="col">
                  <div class="overflow-hidden flex-nowrap">
                    <div class="d-flex justify-content-between">
                      <p class="m-0 mb-1">{{ post.author }}</p>
                      <p class="m-0 mb-1">{{ post.createdDate }}</p>
                    </div>
                    <h6 class="m-0 mb-1 text-start">
                      <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">{{
                          post.title
                        }}
                      </router-link>
                    </h6>
                    <div class="d-flex justify-content-between">
                      <small class="m-0 mb-1"></small>
                      <small class="m-0 mb-1 ">조회수: {{ post.hits }}</small>
                    </div>
                  </div>
                </div>
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