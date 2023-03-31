<template>
  <div class="container d-flex !important justify-content-center">
    <ul class="pagination">
      <li class="page-item" v-if="pagingValues.startPage > 1">
        <router-link class="page-link" :to="getPage(1)">처음</router-link>
      </li>
      <li class="page-item" v-if="pagingValues.currentPage > 1">
        <router-link class="page-link" :to="getPage(pagingValues.currentPage - 1)">이전</router-link>
      </li>
      <template v-for="i in pageRange" v-bind:key="i">
        <li class="page-item">
          <!--
          이 코드가 왜 router를 타지 못했는지 설명

          -->
          <router-link class="page-link" :to="getPage(i)">{{ i }}</router-link>
        </li>
      </template>
      <li class="page-item" v-if="pagingValues.currentPage < pagingValues.totalPage">
        <router-link class="page-link" :to="getPage(pagingValues.currentPage + 1)">다음</router-link>
      </li>
      <!--      페이지네이션의 재활용도를 높이기 위해 이벤트를 발생시키는 방식으로 처리, 현재는 직접 이동시킬 뿐임-->
      <li class="page-item" v-if="pagingValues.endPage < pagingValues.totalPage">
        <router-link class="page-link" :to="getPage(pagingValues.totalPage)">끝</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import {computed} from 'vue';
import {useRoute} from 'vue-router';

export default {
  name: 'Pagination',
  props: {
    // list 내부에 어떤 필드가 있는지 예측할 수 있어야 함
    pagingValues: [],
    pageRange: []
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

    const getPage = (pageNumber) => {
      return {
        name: 'board',
        query: {
          pageNumber,
          ...queryParams.value,
        },
      };
    };

    return {
      getPage
    }
  }
}
</script>
