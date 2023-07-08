## REST API 게시판 프로젝트
특정 아이디어에 집중하지 않고 가장 기본적인 형태가 되는 게시판을 제작하고자 진행한 SPA 게시판 프로젝트입니다.

### 배포 URL
http://ec2-13-209-162-46.ap-northeast-2.compute.amazonaws.com
#### 계정 리스트
1. ID: admin / PW: admin
2. ID: user / PW: user
3. ID: user1 / PW: user1
<hr>

### 기술 스택
#### Frontend
- Vue.js
- Bootstrap
#### Backend
- Java 11
- Spring Boot 2.7.8
- Spring Security
- JWT
- MyBatis
#### Infrastructure
- AWS EC2
- AWS RDS
- Nginx
- MariaDB
<hr>

### 기능 설명
#### 공통
- 공지사항, Q&A, 커뮤니티, 1:1 문의, 이미지 갤러리 구현
- JWT를 사용한 인증/인가(AccessToken만 사용)
- Spring AOP를 이용한 전역 예외 핸들링, 어노테이션 필드 선언식의 사용자 지정 유효성 검증 수행
#### 게시판별 상세 기능
- 공통 - 게시글 CRUD, 댓글 CRUD, 조회수 증가, 파일 업로드/다운로드(이미지 갤러리 제외), 게시판별 검색 기능, 페이징
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
<img width="800" alt="스크린샷 2023-07-08 오전 2 31 58" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/a5c8b3b1-271d-4588-9ac8-579facfdc7b3">

#### 파일 서비스 클래스의 updateFile()메소드
<img width="800" alt="스크린샷 2023-07-08 오전 3 21 57" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/49439887-9c23-4b3e-aafc-2a04ce843035">

#### updateFile() 메소드 내에서 수행되는 deleteFileNotExistsDatabase(), deleteFile() 메소드
<img width="699" alt="스크린샷 2023-07-08 오전 3 12 48" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/6a5d8520-383d-45e2-8f08-1b9097d95710">

#### 이미지 갤러리 컨트롤러
첨부파일 기능을 가진 게시판과는 달리 이미지 갤러리는 이미지만을 업로드하도록 되어 있습니다.<br>
때문에 갤러리 뷰를 통해서 썸네일을 표현해야 하는데, 게시글 작성/수정 시 마다 썸네일을 새로이 등록하는 과정을 수행하도록 구현했습니다.<br>
해당 과정은 주요 과정만 간략하게 나타내기 위해 컨트롤러 이미지만 첨부하였습니다.<br>
<img width="800" alt="스크린샷 2023-07-08 오전 2 37 13" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/29d352b7-1c84-472f-9dea-8714aeadaac3">
<hr>

### API Docs
링크: https://documenter.getpostman.com/view/20624430/2s93zB5McQ

### ERD
링크: https://www.erdcloud.com/d/LXuKoB6WG5itgTh8J
<img width="1993" alt="스크린샷 2023-07-08 오전 3 25 02" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/c9617b37-7bb6-4b34-b7f7-3917d7973e5d">

### 아키텍쳐
<img width="1895" alt="스크린샷 2023-07-08 오후 2 56 38" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/4afed953-11d9-4b00-8c28-cae387669516">
