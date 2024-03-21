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
@ExcelFileName(filename = "lang.pet.health.title.list")
public class HealthDto implements ExcelDto {

    @ExcelColumnName(headerName = "lang.pet.idx")
    private Long idx;
    private Long healthTypeIdx; // health_type.idx
    @ExcelColumnName(headerName = "lang.pet.health")
    private String health;      // 건강질환 이름
    private Integer state;      // 상태값
    private Long sort;          // 정렬값
    @ExcelColumnName(headerName = "lang.pet.regdate")
    private String regDate;     // 등록일

    // 기타
    private String stateText;
    private String stateBg;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, health, regDate);
    }
}
