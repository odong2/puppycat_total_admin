package com.architecture.admin.models.dto.shopping.certification;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificationDto {

    private Integer idx;
    private Integer certificationIdx;   // 인증 정보 idx
    private String certificationName;   // 인증 유형
    private Integer state;              // 상태값
    private String regDate;             // 등록일
    private String regDateTz;           // 등록일 타임존

    private Integer productIdx;         // 상품 Idx
    private String certificationNum;    // 인증 번호

}
