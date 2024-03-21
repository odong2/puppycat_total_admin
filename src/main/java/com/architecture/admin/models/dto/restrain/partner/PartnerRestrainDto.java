package com.architecture.admin.models.dto.restrain.partner;

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
@ExcelFileName(filename = "lang.restrain.partner.title.list")
public class PartnerRestrainDto implements ExcelDto {

    @ExcelColumnName(headerName = "lang.restrain.partner.idx")
    private Integer idx;            // 고유 번호
    private Integer partnerIdx;     // 파트너 번호
    @ExcelColumnName(headerName = "lang.restrain.partner.id")
    private String id;              // 파트너 id
    @ExcelColumnName(headerName = "lang.restrain.partner.company_name")
    private String companyName;     // 상호명
    private Long partnerProductIdx; // 파트너 상품 번호
    @ExcelColumnName(headerName = "lang.restrain.partner.code")
    private String info;            // 제재 사유
    @ExcelColumnName(headerName = "lang.restrain.partner.type")
    private String type;            // 제재 타입
    private Integer date;           // 제재 기간
    private Integer state;          // 상태값
    @ExcelColumnName(headerName = "lang.restrain.partner.state")
    private String stateText;       // 상태 문자변환
    private String stateBg;         // 상태 bg 색상
    @ExcelColumnName(headerName = "lang.restrain.partner.start_date")
    private String startDate;       // 시작일
    @ExcelColumnName(headerName = "lang.restrain.partner.end_date")
    private String endDate;         // 종료일
    @ExcelColumnName(headerName = "lang.restrain.partner.reg_date")
    private String regDate;         // 등록일
    @ExcelColumnName(headerName = "lang.restrain.partner.modi_date")
    private String modiDate;        // 수정일

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, id, companyName, info, type, stateText, startDate, endDate, regDate, modiDate);
    }
}
