<template>
  <div class="container">
    <form class="mb-3">
      <textarea class="form-control mb-3" rows="3" v-model="newComment"></textarea>
      <div class="d-flex justify-content-end">
        <button class="btn btn-primary" type="button" @click="addComment">등록</button>
      </div>
    </form>

    <div class="p-3 mb-3 min-vh-50">
      <div v-for="comment in commentList" :key="comment.commentId">
        <div class="row mb-3">
          <div class="col-sm-9 text-start">
            <slot name="show-adoption" :comment="comment" :adoptedCommentId="adoptedCommentId"></slot>
            <span style="padding-right: 10px;">{{ comment.username }}</span>
            <span style="font-size: 11px">{{ comment.createdDate }} 작성</span>
          </div>
          <div class="col-sm-3 text-end">
            <slot name="adoption" :adoptCommentEvent="adoptCommentEvent" :adoptedCommentId="adoptedCommentId" :comment="comment"></slot>
            <button class="btn btn-outline-primary" v-if="!isEditMode && comment.userId === currentUserInfo.userId"
                    @click="enableEditMode(comment)">수정
            </button>
            <button class="btn btn-outline-secondary" v-if="!isEditMode && comment.userId === currentUserInfo.userId"
                    @click="deleteComment(comment)">삭제
            </button>
          </div>
        </div>
        <div class="row mb-3" v-if="!isEditMode || comment !== editingComment">
          <div class="col-sm-12 text-start">
            <div v-html="comment.comment"></div>
<!--            <textarea class="form-control-plaintext" rows="3" readonly>{{ comment.comment }}</textarea>-->
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
import {computed, getCurrentInstance, onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

export default {
  name: "Comment",
  props: {
    commentList: {},
    currentUserInfo: '',
    authorOfPost: false,
    adoptedCommentId: null,
  },

  setup(props) {
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
      // console.log("modifyComment: " + JSON.stringify(comment))

      // <br>태그로 변환했던 내용을 다시 원래 개행문자로 되돌린다.
      editingComment.value = comment
      editingComment.value.comment = editingComment.value.comment.replace(/<br>/g, '\n')
    }

    const cancelEdit = () => {
      editingComment.value.comment = editingComment.value.comment.replace(/\n/g, '<br>')
      isEditMode.value = false
    }

    const adoptCommentEvent = (comment) => {
      console.log("자식 컴포넌트에서 adoptComment 이벤트 호출, comment: " + JSON.stringify(comment))
      instance.emit("adoptComment", comment)
    }

    return {
      newComment,
      editingComment,
      isEditMode,
      enableEditMode,
      addComment,
      deleteComment,
      modifyComment,
      cancelEdit,
      adoptCommentEvent
    }
  }
}
</script>

<style scoped>
</style>