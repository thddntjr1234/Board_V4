<template>
  <div class="container">
    <h1>게시판 - 목록(page : {{ $route.params.page }})</h1><br>
  </div>
  <div class="container">
    <form class="form-inline">
      <div>
      </div>
      <div class="input-group-sm">
        등록일

        <input name="startDate" class="form-control-sm" type="date" required/>
        <input name="endDate" class="form-control-sm" type="date" required/>

        <select class="form-select-sm" name="category" required>
          <option value="all">전체 카테고리</option>
          <option v-for="category in categoryList" v-bind:key="category" v-bind:value="category.category">
            {{ category.category }}
          </option>
        </select>
        <input type="search" name="keyword">
        <input type="submit" value="검색">
      </div>
    </form>
  </div>
  <br>
  <div class="container">
    <p></p>
  </div>

  <div class="container">
    <table class="table table-hover" style="text-align: center; font-size: 12px">
      <thead>
      <tr>
        <th class="w-auto" style="text-align: center;">카테고리</th>
        <th class="w-auto" style="text-align: center;">&nbsp;</th>
        <th class="w-auto" style="text-align: center;">제목</th>
        <th class="w-auto" style="text-align: center;">작성자</th>
        <th class="w-auto" style="text-align: center;">조회수</th>
        <th class="w-auto" style="text-align: center;">등록 일시</th>
        <th class="w-auto" style="text-align: center;">수정 일시</th>
      </tr>
      </thead>
      <tbody>

      <tr v-for="post in postList" v-bind:key="post">
        <td>{{ post.category }}</td>

        <td v-if="post.fileFlag === true">F</td>
        <td v-else>&nbsp;</td>

        <td class="d-flex justify-content-start">
          <router-link v-bind:to="{path: '/boards/view/' + post.postId}">{{ post.title }}</router-link>
        </td>
        <td>{{ post.author }}</td>
        <td>{{ post.hits }}</td>
        <td>{{ post.createdDate }}</td>
        <td>{{ post.modifiedDate }}</td>
      </tr>
      </tbody>
    </table>
  </div>
  <div class="d-flex justify-content-center">
    <ul class="pagination">
      <li class="page-item" v-if="pagingValues.startPage > 1">
        <router-link v-bind:to="{path: '/boards/list/' + 1}">처음</router-link>
      </li>
      <li class="page-item" v-if="pagingValues.currentPage > 1">
        <router-link v-bind:to="{path: '/boards/list/' + (pagingValues.currentPage - 1)}">이전</router-link>
      </li>
      <template v-for="i in pageRange" v-bind:key="i">
        <li>
          <router-link v-bind:to="{path: '/boards/list/' + i}">{{ i }}</router-link>
        </li>
      </template>
      <li class="page-item" v-if="pagingValues.currentPage < pagingValues.totalPage">
        <router-link v-bind:to="{path: '/boards/list/' + (pagingValues.currentPage + 1)}">다음</router-link>
      </li>
      <li class="page-item" v-if="pagingValues.endPage < pagingValues.totalPage">
        <router-link v-bind:to="{path: '/boards/list/' + pagingValues.totalPage}">끝</router-link>
      </li>
    </ul>
  </div>

  <div class="d-flex justify-content-end">
    <button class="btn btn-secondary" onclick="location.href='/boards/write'">등록</button>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  name: 'Board',
  data() {
    return {
      pageRange: [],
      pagingValues: {},
      categoryList: [],
      postList: [],
    };
  },
  mounted() {
    const list = this.getPostList(this.$route.params.page);
    console.log("route parameter page: " + this.$route.params.page);
    // TODO: 3/4. 비동기를 사용할 때 앞의 메소드가 실행되기 전 밑의 라인이 실행될 수 있다 -> promise + await를 공부해 보자
    console.dir(list);


  },
  methods: {
    getPostList(page) {
      console.log('getPostList()');
      axios.get('/boards/free/list/' + page)
          .then((response) => {
            console.log(response.data);
            this.pagingValues = response.data.pagingValues;
            // TODO: 이 부분은 api를 수정해야 할 것 같다(startpage - endpage 배열로).
            for (let i = this.pagingValues.startPage; i <= this.pagingValues.endPage; i++) {
              this.pageRange.push(i);
            }
            console.log(this.pageRange);
            this.postList = response.data.postList;
            this.categoryList = response.data.categoryList;
          });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
