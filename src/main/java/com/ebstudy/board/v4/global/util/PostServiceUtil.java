package com.ebstudy.board.v4.global.util;

import com.ebstudy.board.v4.dto.PaginationDTO;
import com.ebstudy.board.v4.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                .filter(searchValues.getFilter())
                .secret(searchValues.getSecret())
                .build();
    }

    /**
     * 키워드에서 불필요한 공백을 제거한다.
     * @param keyword 불필요한 공백을 제거할 키워드
     * @return 공백이 제거된 키워드
     */
    public String removeUnnecessarySpaces(String keyword) {
        // null 혹은 공백으로만 이루어졌다면 null을 반환한다.
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        // 연속되는 공백을 하나로 줄인다.
        keyword = keyword.replaceAll("\\s+", " ");

        // 맨 앞과 뒤의 공백을 제거한다.
        keyword = keyword.trim();

        return keyword;
    }
}