<template>
  <div class="container">
    <NavBar></NavBar>
  </div>
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
    <Pagination :paging-values="pagingValues" :page-range="pageRange" @get-page="getPage"/>
    <router-link :to="{name: 'newForm'}">
      <button class="btn btn-secondary">등록</button>
    </router-link>
  </div>
</template>


<script>
import axios from 'axios';
import PostList from "@/components/PostList.vue";
import Pagination from "@/components/Pagination.vue";
import NavBar from "@/components/NavBar.vue";
import SearchForm from "@/components/SearchForm.vue";

// Options API
export default {
  name: 'freeBoardView',
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
      console.log("FreeBoardView getPage() 파라미터: " + pageNumber + ", " + JSON.stringify(queryParams))
      this.$route.query.pageNumber = pageNumber
      this.$route.query.keyword = queryParams.keyword
      this.$route.query.startDate = queryParams.startDate
      this.$route.query.endDate = queryParams.endDate

      console.log("Pagination 컴포넌트 이벤트에 의해 부모 컴포넌트의 getPostList()가 실행됨")
      await this.getPostList()
      // this.$route.query.
    },

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
          ...(this.$route.query.keyword && {keyword: this.$route.query.keyword}),
          ...(this.$route.query.startDate && {startDate: this.$route.query.startDate}),
          ...(this.$route.query.endDate && {endDate: this.$route.query.endDate}),
        }
      })

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
