<template>
  <NavBar></NavBar>
  <div class="container">
    <h1>이미지 갤러리 - 수정</h1>

    <br>

    <div class="row mb-3">
      <label for="title" class="text-start offset-1">제목</label>
      <div class="col-md-10 offset-1">
        <input type="text" class="form-control" id="title" v-model="post.title" placeholder="제목은 4자 이상 100자 미만이어야 합니다"
               required>
      </div>
    </div>

    <br>

    <div class="row mb-3">
      <label for="content" class="text-start offset-1">내용</label>
      <div id="editor" class="col-md-10 offset-1 text-start"></div>
    </div>

    <div class="row mb-3">
      <div class="col-sm">
        <button class="btn btn-primary" @click="modifyPost">저장</button>
        <button class="btn btn-secondary me-2" @click="$router.back()">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onBeforeMount, onMounted} from "vue";
import {ref, reactive} from "vue";
import {useStore} from "vuex"
import {useRoute} from "vue-router";
import '@toast-ui/editor/dist/toastui-editor.css'; // Editor 스타일
import * as boardApi from "@/apis/board"
import * as userApi from "@/apis/user"
import NavBar from "@/components/NavBar.vue";
import Editor from "@toast-ui/editor";
import {saveImage} from "@/apis/board";
import Router from "@/router/router";
import router from "@/router/router";
import {apiErrorHanlder} from "@/error/api-error-hanlder";

const route = useRoute()
const store = useStore()
let editor

// 변수를 ref 혹은 reactive로 감싸면 반응형으로 바뀐다.
const post = ref({})
const addedImageList = ref([])

onBeforeMount(async () => {
  await getPost()
  setEditor()
})

const setEditor = () => {
  Editor.setLanguage(['ko', 'ko-KR'], {
    Markdown: '마크다운',
    WYSIWYG: '위지윅',
    Write: '편집하기',
    Preview: '미리보기',
    Headings: '제목크기',
    Paragraph: '본문',
    Bold: '굵게',
    Italic: '기울임꼴',
    Strike: '취소선',
    Code: '인라인 코드',
    Line: '문단나눔',
    Blockquote: '인용구',
    'Unordered list': '글머리 기호',
    'Ordered list': '번호 매기기',
    Task: '체크박스',
    Indent: '들여쓰기',
    Outdent: '내어쓰기',
    'Insert link': '링크 삽입',
    'Insert CodeBlock': '코드블럭 삽입',
    'Insert table': '표 삽입',
    'Insert image': '이미지 삽입',
    Heading: '제목',
    'Image URL': '이미지 주소',
    'Select image file': '이미지 파일을 선택하세요.',
    'Choose a file': '파일 선택',
    'No file': '선택된 파일 없음',
    Description: '설명',
    OK: '확인',
    More: '더 보기',
    Cancel: '취소',
    File: '파일',
    URL: '주소',
    'Link text': '링크 텍스트',
    'Add row to up': '위에 행 추가',
    'Add row to down': '아래에 행 추가',
    'Add column to left': '왼쪽에 열 추가',
    'Add column to right': '오른쪽에 열 추가',
    'Remove row': '행 삭제',
    'Remove column': '열 삭제',
    'Align column to left': '열 왼쪽 정렬',
    'Align column to center': '열 가운데 정렬',
    'Align column to right': '열 오른쪽 정렬',
    'Remove table': '표 삭제',
    'Would you like to paste as table?': '표형태로 붙여 넣겠습니까?',
    'Text color': '글자 색상',
    'Auto scroll enabled': '자동 스크롤 켜짐',
    'Auto scroll disabled': '자동 스크롤 꺼짐',
    'Choose language': '언어 선택',
  })

  editor = new Editor({
    el: document.querySelector('#editor'),
    language: 'ko-KR',
    height: '600px',
    initialEditType: 'wysiwyg',
    previewStyle: 'tab',
    hideModeSwitch: true,
    hooks: {
      addImageBlobHook: async (blob, callback) => {
        // 1. 이미지 업로드를 먼저 수행한다.
        const uploadResult = await uploadImage(blob);

        console.log(uploadResult.data)
        // 2. 업로드한 이미지 정보를 반환받아 addedImageList에 추가한 후 imgAccessUrl을 콜백한다.
        addedImageList.value.push(uploadResult.data)
        callback(uploadResult.data.imgAccessUrl)
      }
    },
  })

  editor.setHTML(post.value.content)
}

const uploadImage = async (blob) => {
  // 파일 객체이므로 FormData 객체에 추가해서 전송한다.
  const formData = new FormData()
  formData.set("file", blob)

  // 서버로 파일을 전송한다
  return boardApi.saveImage(formData)
}


/**
 * 수정 화면에서 사용할 게시글 정보 요청 메소드
 * @returns post
 */
const getPost = async () => {
  try {
    const response = await boardApi.getPostWithFileList(`boards/gallery/${route.params.postId}/edit`)

    post.value = response.data.post
    response.data.fileList.forEach(file => {
      addedImageList.value.push(file)
    })

    console.log(addedImageList.value)
  } catch (error) {
    apiErrorHanlder(error)
    router.back()
  }
}

/**
 * 게시글 수정
 */
const modifyPost = async () => {
  // editor.getHTML()로 가져온 내용에서 img태그의 src속성값 중 fileName 쿼리 파라미터에 해당하는 값만을 분리한 리스트를 생성한다.
  const fileNameList = parseFileNamesByContent(editor.getHTML())
  const registeredFileList = getImageListByAddedImageList(fileNameList, addedImageList.value)

  const formData = new FormData()

  // registeredFileList를 정상적으로 JSON으로 전달해 파라미터에 바인딩할 수 있도록 Blob 객체로 만든다.
  const json = JSON.stringify(registeredFileList)
  const blob = new Blob([json], {type: "application/json"})

  formData.append("title", post.value.title)
  formData.append("content", editor.getHTML())
  formData.append("registeredFileList", blob)

  try {
    const response = await boardApi.modifyPost(`boards/gallery/${post.value.postId}`, formData)
    alert('게시글을 성공적으로 수정했습니다.')
    router.back()
  } catch (error) {
    apiErrorHanlder(error)
  }
}

/**
 * 에디터 html 코드로부터 img태그의 src속성을 분리한 뒤 fileName 파라미터 값만을 분리한 리스트를 반환하는 메소드
 * @param content 에디터 html 코드
 * @returns {*[]} img 태그로부터 분리한 fileName 파라미터 리스트
 */
function parseFileNamesByContent(content) {
  const parser = new DOMParser()
  const document = parser.parseFromString(content, 'text/html')

  const imgTags = document.getElementsByTagName('img')
  const fileNameList = [];

  // 분리한 이미지 태그 리스트를 순회하면서 fileName 파라미터 값을 분리해 fileNameList에 추가
  for (let i = 0; i < imgTags.length; i++) {
    const src = imgTags[i].getAttribute('src');
    const url = new URL(src)
    const fileName = url.searchParams.get('fileName');

    if (fileName) {
      fileNameList.push(fileName)
    }
  }

  return fileNameList
}

/**
 * 전체 이미지 등록 리스트로부터 실제 등록된 파일에 해당하는 이미지 파일 id값을 가져와 리스트로 반환하는 메소드
 * @param fileNameList 실제로 게시글 내용에 등록된 파일명 리스트
 * @param addedImageList 게시글 내용에 등록되었던 모든 이미지 파일 리스트
 * @returns {*[]} 실제 등록된 이미지 파일의 Id값 리스트
 */
function getImageListByAddedImageList(fileNameList, addedImageList) {
  const registeredImageList = []
  fileNameList.forEach((fileName) => {
    const registeredImage = addedImageList.find((image) => image.fileName === fileName)

    if (registeredImage) {
      registeredImageList.push(registeredImage)
    }
  })

  return registeredImageList
}

</script>

<style scoped>

</style>