# 포트폴리오 스터디
Vue.js, Spring Boot 2.7.8, Java 11, Mybatis, MariaDB, Gradle을 사용한 간단한 REST API 게시판 프로젝트

## 2023-03-04 코드 피드백
> - DTO 필드에도 주석을 달아줘야 이 변수가 어떤 역할을 하는지 유추할 수 있다.
> - 포맷은 보여지는 것에 해당하는 영역이므로 서버는 raw data를 보내주는 게 더 낫다.
> - 비즈니스 로직에서 처리하기 전에 이걸 쿼리로 처리할 수 있는지 확인(checkFileExistence() method)
> - 파일 수정의 경우에는 서버에서 파일도 삭제하도록 하기
> - 데이터는 모두 소중하다. 실제로 DB에서 삭제하는 것이 아니라 boolean처리 등으로 뽑히지 않게만 하고 저장하게 두어야 한다(deletePost() method 부분)
> - GlobalExceptionHandler로 전체 예외에 대해 처리할 수 있도록 하자(400을 던지던지, 500을 던지던지 404를 던지던지 하는 경우에 이걸 그대로 톰캣에서 내뱉도록 하지 않고 Handler에서 캐치해서 처리할 수 있도록)
> - 컨트롤러에서 ResponseEntity의 Body에 각 DTO를 바로 넣지 않고 CommonResponseDTO에 넣어서 반환(전역 에러처리도 이 Response DTO에 에러를 담아서 반환하는 방식)
> - 검색 조건을 추가하게 된다면 PathVariable와 같은 방식으로 검색 조건을 빼야 하는지  쿼리 파라미터로 빼야 하는지 고민하는 게 좋다.
> - 비동기를 사용할 때 앞의 메소드가 실행되기 전 밑의 라인이 실행될 수 있다. → promise, await를 사용해서 이를 방지
