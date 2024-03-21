package com.architecture.admin.models.dto.faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqDto {

    private Integer idx;            // 고유번호
    private Integer menuIdx;        // 메뉴번호
    private String title;           // 제목
    private String adminId;         // 관리자 아이디
    private Integer state;          // 상태값
    private String stateText;       // 상태값 문자변환
    private String stateBg;         // 상태 bg 색상
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존
    private Integer faqIdx;         // FAQ IDX
    private String contents;        // 내용
    private String lang;            // 사용언어
    private String name;            // 이름
    private String menuName;        // 메뉴 이름

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

}
