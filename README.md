# RESTful 게시판 프로젝트
Vue.js 3.2.47, Spring Boot 2.7.8, Java 11, Mybatis 2.3, MariaDB, Gradle을 사용한 간단한 RESTful 게시판 프로젝트

## 주요 기능 설명
- 게시글 작성, 수정, 삭제, 조회(조회수 증가)
- 파일 업로드, 수정, 삭제
- 댓글 작성
- 검색 조건별 게시글 조회 및 페이징
- 반환 데이터에 대한 공통 API Response 포맷
- Spring AOP를 이용한 전역 예외 처리 핸들러, 어노테이션 필드 선언식의 사용자 지정 유효성 검증


## 데이터베이스 ERD
링크: https://www.erdcloud.com/d/LXuKoB6WG5itgTh8J
<iframe width="800" height="400" src="https://www.erdcloud.com/p/LXuKoB6WG5itgTh8J" frameborder="0" allowfullscreen></iframe>

## 코드 피드백
<details>
<summary> 접기/펼치기 </summary>

### 2023-03-04 코드 피드백
- DTO 필드에도 주석을 달아줘야 이 변수가 어떤 역할을 하는지 유추할 수 있다.
- 포맷은 보여지는 것에 해당하는 영역이므로 서버는 raw data를 보내주는 게 더 낫다.
- 비즈니스 로직에서 처리하기 전에 이걸 쿼리로 처리할 수 있는지 확인(checkFileExistence() method)
- 파일 수정의 경우에는 서버에서 파일도 삭제하도록 하기
- 데이터는 모두 소중하다. 실제로 DB에서 삭제하는 것이 아니라 boolean처리 등으로 뽑히지 않게만 하고 저장하게 두어야 한다(deletePost() method 부분)
- GlobalExceptionHandler로 전체 예외에 대해 처리할 수 있도록 하자(400을 던지던지, 500을 던지던지 404를 던지던지 하는 경우에 이걸 그대로 톰캣에서 내뱉도록 하지 않고 Handler에서 캐치해서 처리할 수 있도록)
- 컨트롤러에서 ResponseEntity의 Body에 각 DTO를 바로 넣지 않고 CommonResponseDTO에 넣어서 반환(전역 에러처리도 이 Response DTO에 에러를 담아서 반환하는 방식)
- 검색 조건을 추가하게 된다면 PathVariable와 같은 방식으로 검색 조건을 빼야 하는지  쿼리 파라미터로 빼야 하는지 고민하는 게 좋다.
- 비동기를 사용할 때 앞의 메소드가 실행되기 전 밑의 라인이 실행될 수 있다. → promise, await를 사용해서 이를 방지

### 2023-03-11 코드 피드백
- 유효성 검증은 가급적 컨트롤러 혹은 서비스에서 진행하기, Mapper는 별도의 로직을 가져서는 안 됨.
- SQL에서 where delete_flag문이 항상 따라붙는 것을 치우기 위해 View를 사용해보기
- 값이 자주 변하지 않는 데이터의 경우 캐시로 저장할 수 있도록 하기
- 메서드를 작성할 때는 먼저 주석으로 수행할 기능에 대해 작성하고 시작하면 좋다.

### 2023-03-18 코드 피드백
- 현재 에러 처리나 데이터 반환 등 공통 포맷을 적용해서 사용할 때는 어떤 규격에 대해 먼저 시뮬레이션 해 본 뒤 코드를 작성하는 것이 좋다.
ex) 200, 400, 404시 데이터의 포맷은 어떻게?
- CommonResponse에 데이터를 추가할 때 추가할 객체들을 DTO로 묶어서 담지 말고 Map으로 담자.
- 유효성 검증은 Controller에 파라미터를 전달받으면서 수행하는 것이 좋고, 이 외에 수행해야 한다면 그건 서비스 로직에 해당하는지 확인해야 한다(맞지 않는 Role을 강제로 사용하려고 하는 것일수도 있다.)
- 복잡한 기능 단위가 생기게 되는 경우 이를 메소드로 분리해서 코드의 가독성을 지키기
- 파일 삭제 과정 중 오류가 생기게 되는 상황을 고려해야 한다. → 어떻게 결정할 것인지?
- 뷰 컴포넌트는 단위별로 → 반복된다 라고 생각되는 부분들을 대상으로 하면 좋다. 이 때 컴포넌트에 데이터를 넘겨주는 방법은 다음과 같다.
  1. fetch할 방법(메소드)를 넘겨주기
  2. fetch할 데이터를 넘겨주기
- 컴포넌트와 뷰의 구분 라우터에 노출되는지, 노출되지 않는지로 구분지을 수 있다.
</details>  

## 파일 관련 트러블슈팅
<details>
<summary> 접기/펼치기 </summary>

## 파일 등록 에러

에러 메세지:

Failed to convert property value of type 'java.lang.String' to required type 'java.util.List' for property 'file'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'org.springframework.web.multipart.MultipartFile' for property 'file[0]': no matching editors or conversion strategy found]

자꾸 Vue.js 에서 파일을 FormData에 담고 Content-Type도 multipart/form-data로 설정해서 전송해도
서버의 List<MultipartFile> 필드에 매칭되지 않는다.

원인을 분석해 보니

const files = event.target.files

file.value.push(files[i])

다음과 같은 코드로 작성했을 때, file.value에 대해 로그를 찍어보면

[Object FileList]가 나오게 된다. 즉 file 변수 내에 FileList가 또 존재한다는 말이다.

백엔드 코드는 리스트를 받도록 되어 있지 객체 내에 리스트를 받도록 작성하지 않았기 때문에 매칭되지 않아 오류가 발생하게 된다.

따라서  formData.append(”file”, file.value) 로 전송하는 것이 아니라

```jsx
for ( i = 0 to formData.value.length ) { 
	formData.append(”file”, file.value[i]) 
}
```

와 같이 각각 파일을 append 해 주어야 List<MultipartFile> 형태로 전송된다.

---

<br>

## 파일 등록 후 취소 관련 에러

에러 메세지:

2023-03-31 17:28:36.276 DEBUG 59783 --- [nio-8081-exec-2] o.s.web.method.HandlerMethod : Could not resolve parameter [0] in public com.ebstudy.board.v4.dto.response.CommonApiResponseDTO<?> com.ebstudy.board.v4.controller.CommunityPostController.savePost(com.ebstudy.board.v4.dto.PostDTO) throws java.io.IOException: org.springframework.validation.BeanPropertyBindingResult: 1 errors

Field error in object 'postDTO' on field 'file': rejected value [null]; codes [typeMismatch.postDTO.file,typeMismatch.file,typeMismatch.java.util.List,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [postDTO.file,file]; arguments []; default message [file]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.util.List' for property 'file'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'org.springframework.web.multipart.MultipartFile' for property 'file[0]': no matching editors or conversion strategy found]

`BeanPropertyBindingResult` 에러가 발생했다.

자세한 예외를 찾아보니 postDTO 객체의 List<MultipartFile> file 필드가 String 타입의 데이터를 받아서 `typeMismatch` 에러로 인해 유발되었다. 왜 멀쩡한 필드에 String 타입이 들어갔을까?

### 원인

vue.js 페이지에서 등록하는 부분 html코드는 다음과 같다.

```html
<input type="file" @change="addFile(0, *$event*)" name="file">
```

현재 코드에서는 파일을 취소하게 되면 change 이벤트가 발생하여 addFile 메서드를 수행하게 되는데.

파일 등록 취소의 경우를 처리하기 위해 이 때 들어온 값이 존재하지 않으면 null값을 입력해 주도록 작성되어 있다.

때문에 만약 모든 파일을 등록했다 취소한 뒤 게시글을 등록하게 되면 null 값이 들어간 리스트가 보내지게 되고, 이걸 vue.js에서 String으로 처리해서 보내버리기 때문에 오류가 발생하는 것으로 보인다.

### 첫 번째 시도

기존 Thymeleaf는 input태그의 name 속성을 모두 같게 설정하고 아무것도 입력하지 않은 채 전송하면 null값으로 리스트가 전달되는 것을 생각해

```jsx
const emptyFile = new File([], 'emptyFile.txt', { type: 'text/plain' });
```

이와 같이 더미 파일을 null값 대신 추가하는 방식으로 했었지만! **이는 매우 잘못된 행동이다!**

더미 파일이란 것 자체가 서버를 속이기 위한 용도이고, 올바르지 않은 파일을 전달하는 행위이다.

때문에 더미 파일과 같은 방식은 사용해서는 안 된다.

### **그럼** **어떻게 해결해야 하는가?**

내 생각엔 String[] null값을 서버에서 처리할 수 있는 방법이 존재하지 않는다. String[] 타입으로 들어오는 것을 필드에 바인딩되기 전에 가로채서 처리하는 방식과 같은 것은 오버 엔지니어링이라고 생각하기 때문에 프론트에서 애초에 null list를 보내지 않도록 하는 수 밖에는 없겠다.

팀장님께서는 null을 보내도 서버에서 처리할 수 있어야 한다고 하셨는데, 아마 이런 null이 아니라 List<MultipartFile> file 자체가 아무 값도 없는 이런 상황을 가정하셔서 말씀하신게 아닐까 싶다.

### 두 번째 시도

splice()를 사용해서 잘라내는 방법은, 배열의 Length가 지속적으로 줄어들기 때문에 고정된 인덱스를 사용하는 내 코드에서는 사용하기 껄끄럽다.

때문에 null값을 추가는 하되, 이 것을 savePost() 메소드의 FormData 객체에 추가하는 로직에서 null값을 걸러서 추가하는 방식으로 시도할 것이다.

```jsx
const addFile = (number, event) => {  
	const files = event.target.files
	file.value[number] = files[0] || null 
}
```

savePost() 메소드 내부 로직

```jsx

for (let i = 0; i < file.value.length; i++) {  
	if(file.value[i]) {    
		formData.append("file", file.value[i])  
	}
}
```

위와 같이 null 여부를 체크해서 정상 파일이 있을 때만 append하도록 하고, 이외 경우에는 append하지 않도록 함으로써 일부 취소한 케이스를 포함해 모든 파일을 등록했다 취소한 경우에도 정상적으로 빈 값이 전달되도록 하는 데 성공했다.
</details>
