package com.architecture.admin.models.dto;

import com.architecture.admin.libraries.PaginationLibray;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * 공통 페이징, 검색 Dto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDto {
    // 검색, 페이징
    private String searchType;          // 검색 종류
    private String searchWord;          // 검색어
    private String searchDateType;      // 날짜 검색 종류
    private String searchTargetType;    // 검색 대상자 종류
    private String searchStartDate;     // 검색 시작 날짜
    private String searchEndDate;       // 검색 마지막 날짜
    private String searchSort;          // 정렬
    private Integer state;              // 상태값
    private Integer code;               // 코드
    private Integer show;               // 노출값
    private Integer isDel;              // 회원 탈퇴 상태값
    private Integer isLimit;            // 제한 여부
    private Integer idx;                // idx
    private Long petIdx;                // petIdx
    private String memberUuid;          // 회원 uuid
    private Integer count;              // count
    private Integer type;               // type
    private Integer range;              // range
    private String rangeType;           // range 문자값
    private Integer petType;            // pet type
    private Integer level;              // level`
    private Integer order;              // 정렬
    private PaginationLibray pagination;// 페이징
    private List<String> uuidList;      // 고유아이디 List


    // default paging
    public SearchDto() {
        this.page = 1;
        // 시작번호
        this.offset = 0;
        // DB 조회 갯수
        this.limit = 10;
        // 한 페이지 리스트 수
        this.recordSize = this.limit;
        // 최대 표시 페이징 갯수
        this.pageSize = 10;
    }

    public int getOffset() {
        return (page -1) * recordSize;
    }

    private int page;
    // 시작위치
    private int offset;
    // 리스트 갯수
    @Setter(AccessLevel.PROTECTED)
    private int limit;
    // 한 페이지 리스트 수
    private int recordSize;
    // 최대 표시 페이징 갯수
    private int pageSize;

    /**
     * limit setter 재정의
     *
     * @param limit
     */
    public void setLimit(Integer limit) {
        if (limit == null || limit < 1) {
            this.limit = 10;
        } else if (limit > 1000) { // 최대 max 값 설정
            this.limit = 1000;
            this.recordSize = 1000;
        } else {
            this.limit = limit;
            this.recordSize = limit;
        }
    }

}
