package com.architecture.admin.models.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportCodeDto {

    private Integer idx; // 신고사유 번호
    private String name; // 신고사유
    private Integer state; // 사용상태
}
