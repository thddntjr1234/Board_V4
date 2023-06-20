<template>
  <div class="container">
    <table class="table table-hover" style="text-align: center; font-size: 12px">
      <thead>
      <tr>
        <template v-if="boardName === 'qna' || boardName === 'free'">
          <th class="w-auto" style="text-align: center;">카테고리</th>
          <th class="w-auto" style="text-align: center;" v-if="boardName === 'qna'">채택여부</th>
          <th class="w-auto" style="text-align: center;">&nbsp;</th>
        </template>
        <template v-if="boardName === 'inquiry'">
          <th class="w-auto" style="text-align: center;">답변여부</th>
        </template>
        <th class="w-50" style="text-align: center">제목</th>
        <th class="w-auto" style="text-align: center;">작성자</th>
        <th class="w-auto" style="text-align: center;">조회수</th>
        <th class="w-auto" style="text-align: center;">등록 일시</th>
        <th class="w-auto" style="text-align: center;">수정 일시</th>
      </tr>
      </thead>
      <tbody>
      <!--공지사항 게시글 부분-->
      <tr class="table bg-primary bg-opacity-10" v-for="notice in noticeList" v-bind:key="notice">
        <template v-if="boardName === 'qna' || boardName === 'free' || boardName === 'inquiry'">
          <td>[공지사항]</td>
          <td v-if="boardName === 'qna'"></td>
          <td v-if="boardName === 'qna' || boardName === 'free'"></td>
        </template>
        <td style="text-align: justify">
          <router-link :to="{path: `/boards/notice/${notice.postId}`}">{{ notice.title }}</router-link>
        </td>
        <td>{{ notice.author }}</td>
        <td>{{ notice.hits }}</td>
        <td>{{ notice.createdDate }}</td>
        <td>{{ notice.modifiedDate }}</td>
      </tr>
      <!--각 게시판 게시글 부분-->
      <tr v-for="post in postList" v-bind:key="post">
        <template v-if="boardName === 'qna' || boardName === 'free'">
          <td>{{ post.category }}</td>
          <td v-if="boardName === 'qna'">
            <span v-if="post.adoptedCommentId">&#9989;</span>
            <span v-else>&nbsp</span>
          </td>
          <td>
            <span v-if="post.fileFlag">&#128193;</span>
            <span v-else>&nbsp;</span>
          </td>
        </template>
        <template v-if="boardName === 'inquiry'">
          <td>
            <span v-if="post.answerStatus">답변 완료</span>
            <span v-else>답변 대기중</span>
          </td>
        </template>

        <td style="text-align: justify">
          <span v-if="post.secret" style="padding-right: 5px;">
            &#128274;
          </span>
          <router-link :to="{path: `/boards/${boardName}/${post.postId}`}" class="text-reset">
            {{ post.title }}
            <span v-if="post.commentCount" style="padding-left: 5px">[{{ post.commentCount }}]</span>
          </router-link>

        </td>
        <td>{{ post.author }}</td>
        <td>{{ post.hits }}</td>
        <td>{{ post.createdDate }}</td>
        <td>{{ post.modifiedDate }}</td>
      </tr>
      </tbody>
    </table>
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