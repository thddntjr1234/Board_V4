<template>
  <div class="container d-flex !important justify-content-center">
    <ul class="pagination">
      <li class="page-item" v-if="pagingValues.startPage > 1" @click="getPage(1)">
        <button class="page-link">처음</button>
      </li>
      <li class="page-item" v-if="pagingValues.currentPage > 1" @click="getPage(pagingValues.currentPage - 1)">
            <button class="page-link">이전</button>
      </li>
      <template v-for="i in pageRange" v-bind:key="i">
        <li class="page-item" @click="$emit('get-page', i, queryParams)">
          <button class="page-link">{{ i }}</button>
        </li>
      </template>
      <li class="page-item" v-if="pagingValues.currentPage < pagingValues.totalPage" @click="getPage(pagingValues.currentPage + 1)">
        <button class="page-link">다음</button>
      </li>
      <!--      페이지네이션의 재활용도를 높이기 위해 이벤트를 발생시키는 방식으로 처리, 현재는 직접 이동시킬 뿐임-->
      <li class="page-item" v-if="pagingValues.endPage < pagingValues.totalPage" @click="getPage(pagingValues.totalPage)">
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
    pagingValues: [],
    // 페이지 범위에 대한 숫자 리스트
    pageRange: [],
  },

  setup() {
    const route = useRoute();

    const queryParams = computed(() => {
      const {keyword, categoryId, startDate, endDate} = route.query;
      return {
        keyword,
        categoryId,
        startDate,
        endDate,
      };
    });

    // caught TypeError: Cannot read properties of undefined (reading '$emit')
    // 까먹었는데 컴포지션 API에선 this 사용이 금지된다.
    const instance = getCurrentInstance()
    const getPage = (pageNumber) => {
      instance.emit("get-page", pageNumber, queryParams)
    }

    return {
      getPage,
      queryParams
    }
  }
}
</script>
