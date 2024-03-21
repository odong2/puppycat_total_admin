package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerBankDto {

    private Integer idx;                // idx
    private Integer partnerIdx;         // 파트너 idx
    private Integer bankIdx;            // 은행 idx
    private String accountHolder;       // 예금주
    private String accountNumber;       // 계좌 번호
    private Integer state;              // 0: 거절, 1: 승인, 2: 대기
    private String regDate;             // 등록일 
    private String regDateTz;           // 등록일 타임존

    // 기타
    private String bankName;            // 은행 이름
}
