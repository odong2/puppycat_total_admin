package com.architecture.admin.models.dto.bank;

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
public class BankDto {
    private Integer idx;      // idx
    private String name;      // 은행명
    private Integer sort;     // 정렬
    private Integer state;    // 상태값
    private String regDate;   // 등록일
    private String regDateTz; // 등록일 타임존
}
