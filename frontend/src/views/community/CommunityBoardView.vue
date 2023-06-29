<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>커뮤니티 - 목록</h1><br>
  </div>

  <!--검색-->
  <div class="container">
    <form class="form-inline">
      <div class="input-group input-group-sm">
        <span class="input-group-text">등록일</span>
        <input name="startDate" class="form-control" type="date" v-model="startDate" />
        <input name="endDate" class="form-control" type="date" v-model="endDate" />

        <select class="form-select" name="categoryId" v-model="categoryId">
          <option value="">전체 카테고리</option>
          <option v-for="category in categoryList" :key="category.categoryId" :value="category.categoryId">
            {{ category.category }}
          </option>
        </select>
        <input type="search" name="keyword" class="form-control" v-model="keyword" placeholder="제목/내용/작성자명 키워드" />
        <button class="btn btn-primary" type="submit">검색</button>
      </div>
    </form>
  </div>
  <br>

  <div class="container">
    <div class="d-flex justify-content-between">
      <button class="btn btn-primary" v-show="store.getters.isValidToken" @click="moveToWriteView">게시글 등록</button>

      <div class="btn-group" style="margin-left: auto;">
        <button class="btn btn-outline-primary" @click="togglePostListComponent('list')">리스트</button>
        <button class="btn btn-outline-primary" @click="togglePostListComponent('card')">카드</button>
      </div>
    </div>
  </div>

  <br>
  <div class="container">
    <component :is="currentPostListComponent" :board-name="'free'" :post-list="postList" :notice-list="noticeList"></component>
  </div>
  <br>

  <div class="d-flex justify-content-between">
    <!--   내부의 데이터가 어떤 필드들로 구성되어 있는지 알 수 없으니 풀어서 일일히 선언해주는 것이 좋겟다.-->
    <!--    이벤트 수신은 여기서-->
    <Pagination :paging-values="pagingValues" :page-range="pageRange" @getPage="getPage"/>
  </div>
</template>

<script setup>
import BoardPostList from "@/components/BoardPostList.vue";
import CardPostList from "@/components/CardPostList.vue";
import Pagination from "@/components/Pagination.vue";
import NavBar from "@/components/NavBar.vue";
import router from "@/router/router";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {useStore} from "vuex";
import * as boardApi from "@/apis/board";
import {convertPostListFormat} from "@/utils/format-converter";

const route = useRoute()
const store = useStore()

const pageRange = ref([])
const pagingValues = ref([])
const categoryList = ref([])
const postList = ref([])
const noticeList = ref([])

// 검색 폼 데이터
const keyword = ref(route.query.keyword || null)
const startDate = ref(route.query.startDate || null)
const endDate = ref(route.query.endDate || null)
const categoryId = ref(route.query.categoryId || "")

// 목록 선택
const currentPostListComponent = ref(BoardPostList)

onMounted(() => {
  getFixedNoticeList()
  getPostList()
  getBoardComponentByName(route.query.boardType)
})

/**
 * Pagination 컴포넌트에서 페이지 변경 시 getPostList메소드를 호출하여 페이지를 갱신하기 위한 메소드
 * 기존의 router query를 사용하는 방식으로 검색 조건을 유지하게 한다면 이 방법을 사용할 수 있다.
 * 이외에 vuex를 사용하여 상태 저장소에 쿼리를 넣는 방법도 있음.
 **/
const getPage = async (pageNumber) => {
  // route.query.pageNumber = pageNumber
  // await getPostList()

  router.replace({
    path: route.path, query: {
      pageNumber: pageNumber,
      keyword: route.query.keyword,
      startDate: route.query.startDate,
      endDate: route.query.endDate,
      categoryId: route.query.categoryId,
      boardType: route.query.boardType,
    }
  })
}

/**
 * api 서버로 게시글 리스트를 전송받아 관련 데이터를 설정하는 메소드
 */
const getPostList = async () => {

  const response = await boardApi.getPostList('boards/free', {
    pageNumber: route.query.pageNumber,
    categoryId: route.query.categoryId,

    // spread를 사용하지 않으면 String 타입이라 ""값이 전송돼서 mybatis if문에서 제대로 걸러지지 않는다.
    // 반면 spread를 사용하면 쿼리 파라미터가 존재하지 않을 때 null값을 반환시킨다.
    ...(route.query.keyword && {keyword: route.query.keyword}),
    ...(route.query.startDate && {startDate: route.query.startDate}),
    ...(route.query.endDate && {endDate: route.query.endDate}),
    ...(route.query.filter && {filter: route.query.filter}),
    ...(route.query.secret && {secret: route.query.secret}),
    ...(route.query.sort && {sort: route.query.sort})
  })

  // console.log('response data: ' + JSON.stringify(response.data));
  pagingValues.value = response.data.pagingValues;

  // 배열 초기화
  pageRange.value.length = 0

  // pageRange 값 설정
  for (let i = pagingValues.value.startPage; i <= pagingValues.value.endPage; i++) {
    pageRange.value.push(i);
  }

  // 데이터 입력
  postList.value = convertPostListFormat(response.data.postList)
  categoryList.value = response.data.categoryList
}

const getFixedNoticeList = async () => {
  const response = await boardApi.getFixedNoticeList('community')
  noticeList.value  = convertPostListFormat(response.data)
}

/**
 * 게시글 작성 화면으로 이동하는 메소드
 */
const moveToWriteView = async () => {
  if (!store.getters.isValidToken) {
    alert('게시글 작성은 회원만 가능합니다.')
  } else {
    await router.push({name: 'CommunityWriteFormView'})
  }
}

/**
 * 뷰 모드를 전환하는 메소드
 * @param component
 */
const togglePostListComponent = (component) => {
  // 쿼리 파라미터용으로 사용할 변수
  // getBoardComponentByName(component)

  router.replace({ path: route.path, query: {boardType: component} })
}

/**
 * 컴포넌트 이름을 실제 컴포넌트로 주입하는 메소드
 * @param componentName 컴포넌트 이름 문자열
 */
const getBoardComponentByName = (componentName) => {
  switch (componentName) {
    case 'list':
      currentPostListComponent.value = BoardPostList
      break
    case 'card':
      currentPostListComponent.value = CardPostList
      break
  }
}

</script>

<style scoped>
</style>
