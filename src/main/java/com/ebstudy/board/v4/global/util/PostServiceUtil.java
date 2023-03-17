package com.ebstudy.board.v4.global.util;

import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PostServiceUtil {

    /**
     * 페이징에 필요한 변수값과 현재 페이지에 잘못된 값이 입력된 경우 이를 보정하는 메소드
     *
     * @param totalPostCount 전체 게시글 개수
     * @param searchValues 검색조건
     * @return 페이징에 필요한 시작페이지와 끝 페이지 값과 보정된 요청 페이지 값, 총 페이지 값(끝으로 이동 시 사용), DB LIMIT 시작값
     */
    public PaginationDTO calPagingValues(int totalPostCount, SearchDTO searchValues) {

        int totalPage = totalPostCount / 10;
        if (totalPostCount % 10 > 0) {
            totalPage++;
        }

        Integer currentPage = searchValues.getPageNumber();
        // 유효 페이지 이외의 값을 파라미터로 받게 되는 경우의 예외처리
        // 꼭 이렇게 이상한 값을 넣는 걸 감지하고 예외 처리해야 할까? 그냥 예외를 냅두고 전역으로 처리하는게 어떨까
        if (currentPage == null || currentPage > totalPage || currentPage <= 0) {
            currentPage = 1;
        }

        int startPage = ((currentPage - 1) / 10) * 10 + 1;
        int endPage = startPage + 10 - 1;
        // DB LIMIT 시작값(+1번째부터 10개 추출)
        int startPostNumber = (currentPage - 1) * 10;

        // 마지막 페이지에서 10개 이하로 페이징이 되는 경우 존재하는 만큼만 표시하기 위해 endPage 값 조정
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        return PaginationDTO.builder()
                .startPage(startPage)
                .endPage(endPage)
                .currentPage(currentPage)
                .totalPage(totalPage)
                .startPostNumber(startPostNumber)
                .totalPostCount(totalPostCount)
                .categoryId(searchValues.getCategoryId())
                .keyword(searchValues.getKeyword())
                .startDate(searchValues.getStartDate())
                .endDate(searchValues.getEndDate())
                .build();
    }

    // TODO: 스프링부트에서 보편적으로 사용하는 암호화 방식으로 리팩토링
    /**
     * jsp/servlet 게시판에서 사용하던 단방향 암호화 메소드
     * @param pw 암호화할 패스워드
     * @return 암호화된 패스워드
     */
    public String getSHA512(String pw) {
        String encryptPw = null; //암호화된 비밀번호를 저장할 변수

        //1) 해시 함수를 수행할 객체를 참조할 변수 선언
        MessageDigest md = null;

        try {
            // 2) SHA-512 방식의 해시 함수를 수행할 수 있는 객체를 얻어옴
            md = MessageDigest.getInstance("SHA-512");

            // 3) md를 이용해 암호화를 진행할 수 있도록 pw를 byte[] 배열 형태로 변환
            byte[] bytes = pw.getBytes(Charset.forName("UTF-8"));
            //UTF-8 문자열인거 감안하고 변환해라라는 의미

            // 4) 암호화  -> 암호화 결과가 md 내부에 저장함.
            md.update(bytes);

            // 5) 암호화된 비밀번호를 encryptPw에 대입
            //digest가 보관하고 있다
            //byte[]배열을 String으로 변환할 필요가 있다.
            encryptPw = Base64.getEncoder().encodeToString(md.digest());
            //Base64 : byte 코드를 문자열로 변환하는 객체
            //base64 에서 변환할수 있게 해주는 객체를 가져와 변환한다

        } catch (NoSuchAlgorithmException e) {
            //SHA-512 해시 함수가 존재하지 않을 때 예외 발생
            e.printStackTrace();
        }
        return encryptPw;
    }
}