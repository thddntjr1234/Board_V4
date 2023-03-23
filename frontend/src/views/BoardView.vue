<template>
  <div class="container">
    <h1>게시판 - 목록(page : {{ pagingValues.currentPage }} )</h1><br>
  </div>
  <div class="container">
    <form class="form-inline">
      <div>
      </div>
      <div class="input-group-sm">
        등록일

        <input name="startDate" class="form-control-sm" type="date" :value="this.$route.query.startDate" />
        <input name="endDate" class="form-control-sm" type="date" :value="this.$route.query.endDate" />

        <select class="form-select-sm" name="categoryId" v-model="selectedCategory">
          <option value="">all</option>
          <option v-for="category in categoryList" :key="category" :value="category.categoryId">
            {{ category.category }}
          </option>
        </select>
        <input type="search" name="keyword" :value="this.$route.query.keyword">
        <input type="submit" value="검색">
        {{selectedCategory}}
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
          <router-link :to="{path: '/api/boards/free/' + post.postId}">{{ post.title }}</router-link>
        </td>
        <td>{{ post.author }}</td>
        <td>{{ post.hits }}</td>
        <td>{{ post.createdDate }}</td>
        <td>{{ post.modifiedDate }}</td>
      </tr>
      </tbody>
    </table>
  </div>
  <div class="d-flex justify-content-between">
    <Pagination :paging-values="pagingValues" :page-range="pageRange" />
    <router-link :to="{name: 'newForm'}"><button class="btn btn-secondary">등록</button></router-link>
  </div>
</template>


<script>
import axios from 'axios';
import PostList from "@/components/PostList.vue";
import Pagination from "@/components/Pagination.vue";

export default {
  name: 'Board',
  components: {PostList, Pagination},
  data() {
    return {
      keyword: this.$route.query.keyword ? this.$route.query.keyword : '',
      pageRange: [],
      pagingValues: {},
      categoryList: [],
      postList: [],
      selectedCategory: null
    };
  },

  created() {
    this.getPostList();
  },

  methods: {
    async getPostList() {
      console.log(this.$route.query);
      const response = await axios.get('/api/boards/free', {
        params: {
          pageNumber: this.$route.query.pageNumber,
          categoryId: this.$route.query.categoryId,
          // spread를 사용하지 않으면 String 타입이라 ""값이 전송돼서 mybatis if문에서 제대로 걸러지지 않는다.
          // 반면 spread를 사용하면 쿼리 파라미터가 존재하지 않을 때 null값을 반환시킨다.
          ...(this.$route.query.keyword && { keyword: this.$route.query.keyword }),
          ...(this.$route.query.startDate && { startDate: this.$route.query.startDate }),
          ...(this.$route.query.endDate && { endDate: this.$route.query.endDate }),
        }
      })

      console.log('response data: ' + JSON.stringify(response.data));
      this.pagingValues = response.data.data.pagingValues;

      // pageRange 값 설정
      for (let i = this.pagingValues.startPage; i <= this.pagingValues.endPage; i++) {
        this.pageRange.push(i);
      }

      // data
      this.postList = response.data.data.postList
      this.categoryList = response.data.data.categoryList

      this.selectedCategory = this.getCategoryById()
      console.log("수행 후 selectedCategory: " + this.selectedCategory)
    },

    // categoryList.find가 동작하지 않는 것 같은데
    getCategoryById() {
      console.log("method의 getCategoryById()")
      console.log("categoryId: " + this.$route.query.categoryId)

      const queryCategoryIdParam = parseInt(this.$route.query.categoryId)
      if (!queryCategoryIdParam || String(queryCategoryIdParam) === "") {
        console.log("all 카테고리 반환")
        return "all"
      }

      console.log("categoryList: "  + JSON.stringify(this.categoryList))
      return this.categoryList.find((category) => category.categoryId === queryCategoryIdParam).categoryId
    },
  },
};
</script>

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
