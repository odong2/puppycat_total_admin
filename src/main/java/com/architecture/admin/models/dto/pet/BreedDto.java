package com.architecture.admin.models.dto.pet;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.pet.breed.title.list")
public class BreedDto implements ExcelDto {
    @ExcelColumnName(headerName = "lang.pet.idx")
    private Long idx;
    private Integer typeIdx;  // pet_type.idx
    @ExcelColumnName(headerName = "lang.pet.type")
    private String typeName;  // 반려동물 종류(강아지/고양이)
    @ExcelColumnName(headerName = "lang.pet.breed")
    private String breed;     // 품종
    private Long sort;        // 정렬 순서
    private Integer state;    // 상태값
    @ExcelColumnName(headerName = "lang.pet.regdate")
    private String regDate;   // 등록일

    /**
     * pet
     */
    private String name;      // 반려동물 이름

    /**
     * pet_profile_img
     */
    private String profileUrl;
    private String url;
    private Integer imgWidth;
    private Integer imgHeight;

    /**
     * pet_info
     */
    private Float weight;

    // 기타
    private String stateText;
    private String stateBg;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, typeName, breed, regDate);
    }
}
