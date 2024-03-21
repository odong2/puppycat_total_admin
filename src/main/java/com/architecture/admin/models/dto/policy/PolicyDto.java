package com.architecture.admin.models.dto.policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDto {

    private Integer idx;            // 고유번호
    private Integer policyIdx;      // policy IDX
    private Integer menuIdx;
    private Integer current;
    private String currentText;
    private String title;           // 제목
    private String detail;          // 내용
    private String name;            // 이름
    private String required;        // 필수값 여부
    private Integer category;       // 카테고리 idx
    private Integer state;          // 상태값
    private String stateText;       // 상태값 문자변환
    private String stateBg;         // 상태 bg 색상
    private String lang;            // 사용언어
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존

    private String categoryName;    // 카테고리 이름
    private String menuName;    // 카테고리 이름

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

}
