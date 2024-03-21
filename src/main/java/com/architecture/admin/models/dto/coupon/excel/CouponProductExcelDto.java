package com.architecture.admin.models.dto.coupon.excel;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.libraries.excel.ExcelReadColumnName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.architecture.admin.models.dto.excel.ExcelReadDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcelFileName(filename = "product_sample")
public class CouponProductExcelDto implements ExcelReadDto , ExcelDto{

    @ExcelColumnName(headerName = "lang.coupon.num")
    @ExcelReadColumnName(headerName = "lang.coupon.num")
    private Integer idx;            // 고유번호
    private String productName;     // 상품명
    private Integer result;         // 결과 (1:성공/0:실패)
    private String resultText;      // 결과 문자변환
    private String resultBg;        // 결과 bg 색상

    @Override
    public void fillUpFromRow(Row row) {
        this.idx = (int) row.getCell(0).getNumericCellValue();
    }

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, resultText);
    }
}
