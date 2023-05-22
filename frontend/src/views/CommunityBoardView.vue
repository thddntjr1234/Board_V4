<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>게시판 - 목록(page : {{ pagingValues.currentPage }} )</h1><br>
  </div>
  <SearchForm :category-list="categoryList"></SearchForm>
  <br>
  <div class="container">
    <p></p>
  </div>
  <PostList :post-list="postList"></PostList>
  <div class="d-flex justify-content-between">
    <!--   내부의 데이터가 어떤 필드들로 구성되어 있는지 알 수 없으니 풀어서 일일히 선언해주는 것이 좋겟다.-->
    <!--    이벤트 수신은 여기서-->
    <Pagination :paging-values="pagingValues" :page-range="pageRange" @getPage="getPage"/>

    <button class="btn btn-secondary" @click="moveToWriteView">등록</button>
  </div>
</template>


<script>
import PostList from "@/components/PostList.vue";
import Pagination from "@/components/Pagination.vue";
import NavBar from "@/components/NavBar.vue";
import SearchForm from "@/components/SearchForm.vue";
import store from "@/store/storage";
import router from "@/router/router";
import {useRoute} from "vue-router";
import * as boardApi from "@/apis/board";
import * as userApi from "@/apis/user";

// Options API
export default {
  name: 'CommunityBoardView',
  components: {NavBar, SearchForm, PostList, Pagination},
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
     * Pagination 컴포넌트에서 페이지 변경 시 getPostList메소드를 호출하여 페이지를 갱신하기 위한 메소드
     * 기존의 router query를 사용하는 방식으로 검색 조건을 유지하게 한다면 이 방법을 사용할 수 있다.
     * 이외에 vuex를 사용하여 상태 저장소에 쿼리를 넣는 방법도 있음.
     **/
    async getPage(pageNumber, queryParams) {
      const keyword = queryParams.keyword
      const startDate = queryParams.startDate
      const endDate = queryParams.endDate
      const categoryId = queryParams.categoryId

      await router.push({
        path: this.$route.path,
        query: {
          pageNumber,
          keyword,
          startDate,
          categoryId,
          endDate,
        }
      })
    },

    /**
     * api 서버로 게시글 리스트를 전송받아 관련 데이터를 설정하는 메소드
     */
    async getPostList() {
      const response = await boardApi.getPostList('boards/free')

      // console.log('response data: ' + JSON.stringify(response.data));
      this.pagingValues = response.data.data.pagingValues;

      // 배열 초기화
      this.pageRange.length = 0

      // pageRange 값 설정
      for (let i = this.pagingValues.startPage; i <= this.pagingValues.endPage; i++) {
        this.pageRange.push(i);
      }

      // get 데이터 입력
      this.postList = response.data.data.postList
      this.categoryList = response.data.data.categoryList
    },

    async moveToWriteView() {
      if (!store.getters.isValidToken) {
        alert('게시글 작성은 회원만 가능합니다.')
      }
      await router.push({name: 'CommunityWriteFormView'})
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
