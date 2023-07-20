## SPA 커뮤니티 웹 서비스 프로젝트
특정 아이디어에 집중하지 않고 가장 기본적인 형태가 되는 게시판을 제작하고자 진행한 SPA 커뮤니티 프로젝트입니다.
<hr>

### 배포 URL
http://3.39.111.253
#### 계정 리스트
1. ID: admin / PW: admin
2. ID: user / PW: user
3. ID: user1 / PW: user1
<hr>

### 기술 스택
- **Frontend**
    - Vue.js, Bootstrap 5
- **Backend**
    - Java 11, Spring Boot 2.7.8, Spring Security
    - JWT, MyBatis  
- **Infrastructure**
    - AWS EC2, Nginx
    - AWS RDS, MariaDB
<hr>

### 기능 설명
#### 공통
- 공지사항, Q&A, 커뮤니티, 1:1 문의, 이미지 갤러리 구현
- JWT를 사용한 인증/인가(AccessToken만 사용)
- Spring AOP를 이용한 전역 예외 핸들링, 어노테이션 필드 선언식의 사용자 지정 유효성 검증 수행
#### 게시판별 상세 기능
- 공통 - 게시글 CRUD, 댓글 CRUD, 조회수 증가, 파일 업로드/다운로드(이미지 갤러리는 이미지 업로드/다운로드), 게시판별 검색 기능, 페이징
- 공지사항
  - 상단 게시글 고정(대상 게시판 지정)
  - 관리자만 게시글 작성 가능
  - 댓글 카운트 표시
- Q&A
  - 답변된 댓글 채택
  - 채택된 게시글 표시
- 커뮤니티
  - 공통 기능과 동일
- 이미지 갤러리
  - 썸네일 이미지 자동 생성
  - 텍스트 에디터를 사용한 이미지 추가
  - 작성/수정 시 게시글에 사용되지 않은 이미지 삭제 처리
- 1:1 문의
  - 비밀글 설정 및 답변 여부 표시
  - 관리자가 댓글 작성시 답변완료 표시
  - 답변 완료된 게시글은 수정/삭제 불가 처리
<hr>

### 동작 화면
대략적인 동작 화면은 다음과 같습니다.
| 로그인 | 이미지 게시판 |
|--------|--------------|
| ![로그인](https://github.com/thddntjr1234/Board_V4/assets/29126159/dc497771-608b-4471-899c-fbd8fa360cca) | ![이미지 게시판](https://github.com/thddntjr1234/Board_V4/assets/29126159/6cafa181-b661-45d4-9cf1-7cd056eb0407) |

| 댓글 기능 | Q&A 게시판 댓글 채택 |
|----------|----------------------|
| ![댓글 기능](https://github.com/thddntjr1234/Board_V4/assets/29126159/41bb1732-0299-46bb-87af-80e8cd1f1f86) | ![Q&A 게시판 댓글 채택](https://github.com/thddntjr1234/Board_V4/assets/29126159/9d515213-b932-4f1e-a7cb-0d7697a6b201) |
<hr>

### 저는 이러한 방식으로 코드를 작성합니다.
#### 파일 첨부기능을 가진 게시글 수정 과정 소개
컨트롤러 계층은 주요 비즈니스 로직별로 메소드를 수행하도록 순서를 정의합니다.<br>
또한 서비스 계층에서는 각 기능별로 메소드로 분리한 뒤, 이를 하나의 비즈니스 로직으로 만들어 제공하도록 구현하였습니다.<br>

#### 컨트롤러
```java
/**
     * 게시글 수정
     *
     * @param post 수정할 내용이 담긴 게시글 DTO
     * @return ResponseEntity 객체
     */
    @PutMapping("/api/boards/free/{postId}")
    public ResponseEntity<Object> updatePost(@CustomValidation(value = {"title", "content"}) @ModelAttribute PostDTO post,
                                              @RequestPart(required = false) List<FileDTO> existingFiles) throws IOException {
        // Multipart/Form-Data 방식과 json타입의 객체를 같이 사용하려면 json파트에 대해 @RequestPart 어노테이션을 적용해 주면 된다.

        // 수정 요청한 게시글 데이터를 DB에서 가져와서 해당 정보 기준으로 비교한다(postId를 다르게 하고 authorId를 일치시키는 방식으로 위조할 수 있기 때문에)
        PostDTO originPost = postService.getPost(post.getPostId());
        userService.verifySameUser(originPost.getAuthorId());

        // 게시글 먼저 수정
        postService.updatePost(post);
        fileService.updateFile(post.getPostId(), existingFiles, post.getFile());

        return ResponseEntity.ok(null);
    }
```

#### 파일 서비스 클래스의 updateFile()메소드
```java
/**
     * 입력된 파일 리스트를 참조하여 현재 DB와 파일 저장소의 파일을 수정
     *
     * @param postId            수정할 파일들의 부모 게시글 id
     * @param deliveryFiles     입력된 기존 파일
     * @param multipartFileList 새로 입력할 파일(수정됨, 혹은 추가)
     */
    public void updateFile(Long postId, List<FileDTO> deliveryFiles, List<MultipartFile> multipartFileList) throws IOException {

        // 기존 파일 리스트 로딩
        List<FileDTO> originFiles = fileMapper.getFileList(postId);

        deleteFileNotExistsDatabase(deliveryFiles, originFiles);

        // 기존 파일 외 신규 입력되는 파일에 대해 저장
        saveFile(postId, multipartFileList);
    }
```

#### updateFile() 메소드 내에서 수행되는 deleteFileNotExistsDatabase(), deleteFile() 메소드
```java
/**
     * 수정 시 입력으로 전송된 FileDTO 리스트에 해당하지 않는 파일들을 DB 및 파일 저장소에서 제거
     *
     * @param existingFiles 게시글에서 보낸 기존 파일 리스트
     * @param originFiles   DB상의 기존 파일 리스트
     */
    private void deleteFileNotExistsDatabase(List<FileDTO> existingFiles, List<FileDTO> originFiles) {

        // [file, null, file]과 같이 전달된 경우 NPE가 발생할 수 있으므로 이를 제거
        existingFiles.removeAll(Collections.singletonList(null));

        if (originFiles.isEmpty()) {
            return;
        }

        // existingFiles에서 UUID가 추가된 파일명만 분리한 리스트를 생성
        List<String> existingFileNames = existingFiles.stream()
                .map(FileDTO::getFileName)
                .collect(Collectors.toList());

        // originFiles를 순회하며 existingFileNames에 없는 파일들을 삭제
        originFiles.stream()
                .filter(originFile -> !existingFileNames.contains(originFile.getFileName()))
                .forEach(this::deleteFile);
    }

    /**
     * 요청한 파일을 DB와 실제 파일 저장소에서 모두 삭제한다.
     *
     * @param file 전달한 파일의 정보
     */
    public void deleteFile(FileDTO file) {

        String filePath = basicPath + file.getFileName();
        File targetFile = new File(filePath);

        // 삭제 과정중 발생하는 오류에 대해 처리할 수 있도록 수정
        if (targetFile.exists()) {
            if (!targetFile.delete()) {
                throw new CustomException(CustomErrorCode.FAILED_FILE_DELETE);
            }
        }

        fileMapper.deleteFile(file.getFileId());
    }
```

#### 이미지 갤러리 컨트롤러
첨부파일 기능을 가진 게시판과는 달리 이미지 갤러리는 이미지만을 업로드하도록 되어 있습니다.<br>
때문에 갤러리 뷰를 통해서 썸네일을 표현해야 하는데, 게시글 작성/수정 시 마다 썸네일을 새로이 등록하는 과정을 수행하도록 구현했습니다.<br>
해당 과정은 주요 과정만 간략하게 나타내기 위해 컨트롤러 메소드만 첨부하였습니다.<br>
```java
/**
     * 수정 시 입력으로 전송된 FileDTO 리스트에 해당하지 않는 파일들을 DB 및 파일 저장소에서 제거
     *
     * @param existingFiles 게시글에서 보낸 기존 파일 리스트
     * @param originFiles   DB상의 기존 파일 리스트
     */
    private void deleteFileNotExistsDatabase(List<FileDTO> existingFiles, List<FileDTO> originFiles) {

        // [file, null, file]과 같이 전달된 경우 NPE가 발생할 수 있으므로 이를 제거
        existingFiles.removeAll(Collections.singletonList(null));

        if (originFiles.isEmpty()) {
            return;
        }

        // existingFiles에서 UUID가 추가된 파일명만 분리한 리스트를 생성
        List<String> existingFileNames = existingFiles.stream()
                .map(FileDTO::getFileName)
                .collect(Collectors.toList());

        // originFiles를 순회하며 existingFileNames에 없는 파일들을 삭제
        originFiles.stream()
                .filter(originFile -> !existingFileNames.contains(originFile.getFileName()))
                .forEach(this::deleteFile);
    }

    /**
     * 요청한 파일을 DB와 실제 파일 저장소에서 모두 삭제한다.
     *
     * @param file 전달한 파일의 정보
     */
    public void deleteFile(FileDTO file) {

        String filePath = basicPath + file.getFileName();
        File targetFile = new File(filePath);

        // 삭제 과정중 발생하는 오류에 대해 처리할 수 있도록 수정
        if (targetFile.exists()) {
            if (!targetFile.delete()) {
                throw new CustomException(CustomErrorCode.FAILED_FILE_DELETE);
            }
        }

        fileMapper.deleteFile(file.getFileId());
    }
```
<hr>

### API Docs
링크: https://documenter.getpostman.com/view/20624430/2s93zB5McQ
<hr>

### ERD
링크: https://www.erdcloud.com/d/LXuKoB6WG5itgTh8J
<img width="1993" alt="스크린샷 2023-07-08 오전 3 25 02" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/c9617b37-7bb6-4b34-b7f7-3917d7973e5d">
<hr>

### 아키텍쳐
<img width="1895" alt="스크린샷 2023-07-08 오후 2 56 38" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/4afed953-11d9-4b00-8c28-cae387669516">
