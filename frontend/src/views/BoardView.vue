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
          <option value="">전체 카테고리</option>
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
      // 검색버튼 클릭 시 담기는 검색조건 변수
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

    /**
     * api 서버로 게시글 리스트를 전송받아 관련 데이터를 설정하는 메소드
     */
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

      // get 데이터 입력
      this.postList = response.data.data.postList
      this.categoryList = response.data.data.categoryList

      // 입력받은 categoryId 쿼리 파라미터에 맞는 카테고리를 설정
      this.selectedCategory = this.getCategoryById()
      console.log("selectedCategory is: " + this.selectedCategory)
    },

    /**
     * 카테고리 Id값에 해당하는 카테고리 이름을 data()의 categoryList에서 찾아 반환하는 메소드
     * @returns 카테고리명
     */
    getCategoryById() {
      const queryCategoryIdParam = parseInt(this.$route.query.categoryId)

      // all 카테고리 선택시 빈 값을 전송하므로 별도로 all을 반환
      if (!queryCategoryIdParam) {
        console.log("all 카테고리 value 반환")
        return ""
      }

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
