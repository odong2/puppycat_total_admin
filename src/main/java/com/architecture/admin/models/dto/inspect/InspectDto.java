package com.architecture.admin.models.dto.inspect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectDto {
    private String message;            // 점검 / 업데이트 내용
    private String startDate;       // 시작 날짜
    private String endDate;       // 시작 날짜
    private List targetServiceList;
}
