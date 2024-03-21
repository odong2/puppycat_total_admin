package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerInfoDto {
    /**
     * partner_info
     */
    private Integer idx;                // idx
    private String txseq;               // 본인인증거래번호
    private String name;                // 본인인증 이름
    private String phone;               // 전화번호
    private String ci;                  // 개인 식별 고유값
    private String di;                  // di
    private String ip;                  // 인증 아이피
    private String regDate;             // 등록일
    private String regDateTz;           // 등록일 타임존
}
