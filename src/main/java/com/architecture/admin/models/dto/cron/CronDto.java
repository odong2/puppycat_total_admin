package com.architecture.admin.models.dto.cron;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CronDto {

    private Long idx;   // 일련번호
    private String name;    // 이름
    private String detail;  // 설명
    private String regularExpression;   // 정규표현식
    private Integer state;  // 상태값
    private String stateText;  // 상태값 문자변환
    private String stateBg; // 상태버튼 색상
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존

}
