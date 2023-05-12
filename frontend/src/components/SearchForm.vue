<template>
  <div class="container">
    <form class="form-inline">
      <div>
      </div>
      <div class="input-group-sm">
        등록일

        <input name="startDate" class="form-control-sm" type="date" :value="startDate"/>
        <input name="endDate" class="form-control-sm" type="date" :value="endDate"/>

        <!--        카테고리는 store을 사용해서 저장-->
        <select class="form-select-sm" name="categoryId" v-model="categoryId" @change="categoryId">
          <option value="">전체 카테고리</option>
          <option v-for="category in categoryList" :key="category" :value="category.categoryId">
            {{ category.category }}
          </option>
        </select>
        <input type="search" name="keyword" :value="keyword">
        <input type="submit" value="검색" placeholder="제목/내용/작성자명 키워드">
      </div>
    </form>
  </div>
</template>

<script>
import {ref, reactive} from "vue"
import {useRouter, useRoute} from 'vue-router'

export default {
  name: "SearchForm",

  props: {
    categoryList: []
  },

  setup() {
    const route = useRoute()
    const keyword = ref(route.query.keyword || null)
    const startDate = ref(route.query.startDate || null)
    const endDate = ref(route.query.endDate || null )
    const categoryId = ref(route.query.categoryId || "")

      return {
        keyword,
        startDate,
        categoryId,
        endDate
      };
  }
}
</script>

<style scoped>

</style>