<template>
  <div class="container">
    <table class="table table-hover" style="text-align: center; font-size: 12px">
      <thead>
      <tr>
        <th class="w-auto" style="text-align: center;">카테고리</th>
        <th class="w-auto" style="text-align: center;" v-if="showQnA">채택여부</th>
        <th class="w-auto" style="text-align: center;">&nbsp;</th>
        <th class="w-50" style="text-align: center">제목</th>
        <th class="w-auto" style="text-align: center;">작성자</th>
        <th class="w-auto" style="text-align: center;">조회수</th>
        <th class="w-auto" style="text-align: center;">등록 일시</th>
        <th class="w-auto" style="text-align: center;">수정 일시</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="post in postList" v-bind:key="post">
        <td>{{ post.category }}</td>

        <td v-if="showQnA">
          <span v-if="post.adoptedCommentId">&#9989;</span>
          <span v-else>&nbsp</span>
        </td>
        <td v-if="post.fileFlag">&#128193;</td>
        <td v-else>&nbsp</td>

        <td class="d-flex justify-content-start">
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

<script>
export default {
  name: "PostList",

  props: {
    boardName: '',
    postList: [],
    showQnA: false
  },

  setup() {
    const convertDateFormat = () => {

    }

  }
}
</script>

<style scoped>

</style>