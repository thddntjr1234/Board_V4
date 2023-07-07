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
### 동작 화면
#### 로그인
![로그인_AdobeExpress](https://github.com/thddntjr1234/Board_V4/assets/29126159/4bd8a7b2-2736-47bd-915a-3f2f9d0f506f)
#### 이미지 게시판
![이미지_게시판_AdobeExpress](https://github.com/thddntjr1234/Board_V4/assets/29126159/fc513ecd-e1a1-4688-862b-fdd2325a7ebe)
### 댓글 기능
![댓글_AdobeExpress](https://github.com/thddntjr1234/Board_V4/assets/29126159/146c8237-b429-4886-8d5c-910de91c4769)
### Q&A 게시판 댓글 채택
![Q_A_채택_AdobeExpress](https://github.com/thddntjr1234/Board_V4/assets/29126159/78bfedfd-ad78-4176-a64e-b74ebfcd2336)
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

