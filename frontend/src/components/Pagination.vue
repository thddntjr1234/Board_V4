<template>
  <div class="container d-flex justify-content-center">
    <ul class="pagination">
      <li class="page-item" v-if="pagingValues.startPage > 1" @click="getPage(1)">
        <button class="page-link">처음</button>
      </li>
      <li class="page-item" v-if="pagingValues.currentPage > 1" @click="getPage(pagingValues.currentPage - 1)">
            <button class="page-link">이전</button>
      </li>
      <template v-for="i in pageRange" v-bind:key="i">
        <li :class="['page-item', { 'active': i === pagingValues.currentPage }]" @click="getPage(i)">
          <button class="page-link">{{ i }}</button>
        </li>
      </template>
      <li class="page-item" v-if="pagingValues.currentPage < pagingValues.totalPage" @click="getPage(pagingValues.currentPage + 1)">
        <button class="page-link">다음</button>
      </li>
      <li class="page-item" v-if="pagingValues.endPage < pagingValues.totalPage" @click="getPage(pagingValues.totalPage)">
        <button class="page-link">끝</button>
      </li>
    </ul>
  </div>
</template>

<script>
import {getCurrentInstance} from 'vue';

export default {
  name: 'Pagination',
  props: {
    // totalPostCount, start&end&current&total Page, startPostNumber
    pagingValues: {},
    // 페이지 범위에 대한 숫자 리스트
    pageRange: [],
  },

  setup() {
    const instance = getCurrentInstance()
    const getPage = (pageNumber) => {
      instance.emit("getPage", pageNumber)
    }

    return {
      getPage,
    }
  }
}
</script>
