## REST API 게시판 프로젝트
특정 아이디어에 집중하지 않고 가장 기본적인 형태가 되는 게시판을 제작하고자 진행한 SPA 게시판 프로젝트입니다.

### 배포 URL
http://ec2-13-209-162-46.ap-northeast-2.compute.amazonaws.com

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

### 코드 소개
게시글 수정 과정 중 파일 교체 과정을 구현한 코드입니다.
컨트롤러 계층은 주요 비즈니스 로직별로 메소드를 수행하도록 순서를 정의합니다.
또한 서비스 계층에서는 각 기능별로 메소드로 분리한 뒤, 이를 하나의 비즈니스 로직으로 만들어 제공하도록 구현하였습니다.

#### 컨트롤러
<img width="1085" alt="스크린샷 2023-07-08 오전 2 31 58" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/a5c8b3b1-271d-4588-9ac8-579facfdc7b3">

#### 파일 서비스 클래스의 updateFile()메소드
<img width="888" alt="스크린샷 2023-07-08 오전 2 32 54" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/999201b3-dd20-40c4-980b-b84313cb6d73">

#### updateFile() 메소드 내에서 수행되는 deleteFileNotExistsDatabase(), deleteFile() 메소드
<img width="718" alt="스크린샷 2023-07-08 오전 2 34 07" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/00bdb7eb-8b8a-4bc3-aef3-587bdc0538da">

또한 이미지 갤러리 컨트롤러에서는 썸네일 등록 과정과 DB상에서 기존 썸네일을 제거하는 과정들을 수행합니다.
<img width="1097" alt="스크린샷 2023-07-08 오전 2 37 13" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/29d352b7-1c84-472f-9dea-8714aeadaac3">
<hr>



### 동작 화면
대략적인 동작 화면은 다음과 같습니다.
#### 로그인
![로그인](https://github.com/thddntjr1234/Board_V4/assets/29126159/dc497771-608b-4471-899c-fbd8fa360cca)

#### 이미지 게시판
![이미지-게시판](https://github.com/thddntjr1234/Board_V4/assets/29126159/6cafa181-b661-45d4-9cf1-7cd056eb0407)

### 댓글 기능
![댓글](https://github.com/thddntjr1234/Board_V4/assets/29126159/41bb1732-0299-46bb-87af-80e8cd1f1f86)

### Q&A 게시판 댓글 채택
![Q_A-채택](https://github.com/thddntjr1234/Board_V4/assets/29126159/9d515213-b932-4f1e-a7cb-0d7697a6b201)
<hr>

### API Docs
링크: https://documenter.getpostman.com/view/20624430/2s93zB5McQ
<hr>

### ERD
링크: https://www.erdcloud.com/d/LXuKoB6WG5itgTh8J
<img width="1908" alt="스크린샷 2023-06-25 오전 11 53 16" src="https://github.com/thddntjr1234/Board_V4/assets/29126159/e8c6a2c1-a3e2-47f9-93e2-197653b0c6d0">
<hr>

### 아키텍쳐
AWS 배포 후 추가 예정

