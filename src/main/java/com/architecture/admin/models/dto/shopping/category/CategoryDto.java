package com.architecture.admin.models.dto.shopping.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {

    private Integer idx;        // shop_product_category.idx
    private Integer categoryMappingIdx; // shop_product_category_mapping.idx
    private Integer parent;     // 상위 카테고리 idx
    private Integer depth;      // 단계
    private String code;        // 코드
    private String category;    // 이름
    private Integer groupNum;   // 그룹 번호
    private Integer main;       // 대표 카테고리 여부
    private Integer sort;       // 순서
    private Integer state;      // 상태값
    private String modiDate;    // 수정일
    private String modiDateTz;  // 수정일 타임존
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존

    private List<CategoryDto> childrenCategory; // 하위 카테고리 리스트

    // sql
    private Integer insertedId;

}
