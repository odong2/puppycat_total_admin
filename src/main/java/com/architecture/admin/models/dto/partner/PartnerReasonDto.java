package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerReasonDto {
    private Integer idx;        // idx
    private Integer partnerIdx; // 파트너 idx
    private String reason;      // 등록반려사유
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존
}
