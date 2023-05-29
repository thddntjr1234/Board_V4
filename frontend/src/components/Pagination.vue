<template>
  <div class="container d-flex justify-content-center">
    <ul class="pagination">
      <li class="page-item" v-if="pagingValues.startPage > 1" @click="getPage(1, queryParams)">
        <button class="page-link">처음</button>
      </li>
      <li class="page-item" v-if="pagingValues.currentPage > 1" @click="getPage(pagingValues.currentPage - 1, queryParams)">
            <button class="page-link">이전</button>
      </li>
      <template v-for="i in pageRange" v-bind:key="i">
        <li class="page-item" @click="getPage(i, queryParams)">
          <button class="page-link">{{ i }}</button>
        </li>
      </template>
      <li class="page-item" v-if="pagingValues.currentPage < pagingValues.totalPage" @click="getPage(pagingValues.currentPage + 1, queryParams)">
        <button class="page-link">다음</button>
      </li>
      <li class="page-item" v-if="pagingValues.endPage < pagingValues.totalPage" @click="getPage(pagingValues.totalPage, queryParams)">
        <button class="page-link">끝</button>
      </li>
    </ul>
  </div>
</template>

<script>
import {computed, getCurrentInstance} from 'vue';
import {useRoute} from 'vue-router';

export default {
  name: 'Pagination',
  props: {
    // totalPostCount, start&end&current&total Page, startPostNumber
    pagingValues: {},
    // 페이지 범위에 대한 숫자 리스트
    pageRange: [],
  },

  setup() {
    const route = useRoute();

    const queryParams = computed(() => {
      const {keyword, categoryId, startDate, endDate, boardType} = route.query;
      return {
        keyword,
        categoryId,
        startDate,
        endDate,
        boardType
      };
    });

    const instance = getCurrentInstance()
    const getPage = (pageNumber, queryParams) => {
      instance.emit("getPage", pageNumber, queryParams)
    }

    return {
      getPage,
      queryParams
    }
  }
}
</script>
