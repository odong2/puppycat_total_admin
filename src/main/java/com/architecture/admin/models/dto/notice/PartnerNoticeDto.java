package com.architecture.admin.models.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerNoticeDto {

    private Integer idx;            // 고유번호
    private Integer menuIdx;        // 메뉴번호
    private Integer adminIdx;       // 관리자 고유값
    private Integer isTop;          // 상단공지 0:기본 1:출력
    private String isTopBg;         // 상단공지 bg 색상
    private String isTopText;       // 상단공지 문자변환
    private String title;           // 제목
    private Integer state;          // 상태값
    private String stateText;       // 상태값 문자변환
    private String stateBg;         // 상태 bg 색상
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존
    private Integer noticeIdx;      // 공지IDX
    private String contents;        // 내용
    private String name;            // 이름
    private String menuName;        // 메뉴 이름

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;
}
