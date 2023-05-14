<template>
  <div class="container">
    <form class="mb-3">
      <textarea class="form-control mb-3" rows="3" v-model="newComment"></textarea>
      <div class="d-flex justify-content-end">
        <button class="btn btn-primary" type="button" @click="addComment">등록</button>
      </div>
    </form>

    <div class="p-3 mb-3 min-vh-50">
      <div v-for="comment in commentList" :key="comment.id">
        <div class="row mb-3">
          <div class="col-sm-9 text-start">
            <span>{{ comment.username }}</span>
            <span>{{ comment.createdDate }}</span>
          </div>
          <div class="col-sm-3 text-end">
            <button class="btn btn-outline-primary" v-if="!isEditMode && comment.userId === currentUserInfo.userId"
                    @click="enableEditMode(comment)">수정
            </button>
            <button class="btn btn-outline-secondary" v-if="!isEditMode && comment.userId === currentUserInfo.userId"
                    @click="deleteComment(comment)">삭제
            </button>
          </div>
        </div>
        <div class="row mb-3" v-if="!isEditMode || comment !== editingComment">
          <div class="col-sm-12">
            <p class="text-start">{{ comment.comment }}</p>
          </div>
        </div>
        <div class="row mb-3" v-if="isEditMode && comment === editingComment">
          <div class="col-sm-12">
            <textarea class="form-control mb-3" rows="3" v-model="editingComment.comment"></textarea>
            <button class="btn btn-primary" type="button" @click="modifyComment">저장</button>
            <button class="btn btn-outline-secondary" type="button" @click="cancelEdit">취소</button>
          </div>
        </div>
        <hr>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, getCurrentInstance, ref} from 'vue';
import {useRoute} from 'vue-router';

export default {
  name: "Comment",
  props: {
    commentList: {},
    currentUserInfo: '',
  },

  setup() {
    const newComment = ref('')
    const editingComment = ref('')
    const isEditMode = ref(false)


    const instance = getCurrentInstance()
    const addComment = () => {
      instance.emit("addComment", newComment)
    }

    const deleteComment = (comment) => {
      console.log("delete comment raw: " + JSON.stringify(comment) + ",  value: " + JSON.stringify(comment.value))
      instance.emit("deleteComment", comment)
    }

    const modifyComment = () => {
      instance.emit("modifyComment", editingComment.value)
    }

    const enableEditMode = (comment) => {
      isEditMode.value = true
      editingComment.value = comment
    }

    const cancelEdit = () => {
      isEditMode.value = false
    }

    return {
      newComment,
      editingComment,
      isEditMode,
      enableEditMode,
      addComment,
      deleteComment,
      modifyComment,
      cancelEdit
    }
  }
}
</script>

<style scoped>

</style>