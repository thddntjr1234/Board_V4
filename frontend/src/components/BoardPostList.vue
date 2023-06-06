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

        <td class="d-flex justify-content-start">
          <span v-if="post.secret" style="padding-right: 5px;">
            &#128274;
          </span>
          <router-link :to="{path: `/boards/${boardName}/${post.postId}`}">{{ post.title }}</router-link>
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
})

</script>

<style scoped>

</style>