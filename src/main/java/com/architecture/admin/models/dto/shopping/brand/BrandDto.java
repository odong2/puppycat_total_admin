package com.architecture.admin.models.dto.shopping.brand;

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
public class BrandDto {

    private Integer idx;        // 브랜드 번호
    private String brandName;   // 브랜드명
    private Integer state;      // 상태값
    private String modiDate;    // 수정일
    private String modiDateTz;  // 수정일 타임존
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존


    private Integer brandGroupIdx;  // 브랜드 그룹 idx
    private String brandGroupName;  // 브랜드 그룹 이름
    private Integer type;       // 브랜드 그룹(0:ㄱㄴㄷ)

    // sql
    private Integer insertedId;
}
