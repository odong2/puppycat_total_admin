package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerHolidayDto {
    private Integer idx;        // idx
    private Integer partnerIdx; // 파트너 idx
    private String name;        // 일정명
    private String description; // 설명
    private Integer state;      // 상태값
    private String startDate;   // 시작일
    private String endDate;     // 종료일
    private String regDate;     // 등록일
    private Integer publicHolidaysUsed; // 국가 공휴일 사용
    private String modiDate;            // 수정일

    // 기타
    private String type; // 타입 (partner : 파트너 휴무일, public : 법정 공휴일)
    private List<PartnerHolidayDto> searchConditionList; // 날짜 검색용
    private String searchYear;
}
