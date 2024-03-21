package com.architecture.admin.models.dto.shopping.attributeSet;

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
public class AttributeSetDto {

    private Integer idx;                // 상품 정보 고시 idx(attribute_set.idx)
    private String attributeSetName;    // 상품 정보 고시 유형(attribute_set.attribute_set_name)
    private Integer state;              // 상태값
    private String regDate;             // 등록일
    private String regDateTz;           // 등록일 타임존

    private Integer attributeIdx;       // 상품 정보 고시 표기내용(attribute_name.idx)
    private String attributeName;       // 상품 정보 고시 표기명(attribute_name.attribute_name)
    private String attributeValue;      // 상품 정보 고시 값(attribute_mapping.attribute_value)
    private Integer attributeMappingIdx; // 상품 정보 고시 매핑 idx
}
