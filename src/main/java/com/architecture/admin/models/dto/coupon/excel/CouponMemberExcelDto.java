package com.architecture.admin.models.dto.coupon.excel;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelReadDto;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

@Data
@ExcelFileName(filename = "member_sample_file")
public class CouponMemberExcelDto implements ExcelReadDto {

    @ExcelColumnName(headerName = "lang.coupon.num")
    private Integer idx; // 고유번호
    @ExcelColumnName(headerName = "lang.coupon.member.email")
    private String email; // 이메일
    @ExcelColumnName(headerName = "lang.coupon.member.phone_number")
    private String phone; // 연락처

    @Override
    public void fillUpFromRow(Row row) {
        this.idx = (int) row.getCell(0).getNumericCellValue();
        this.email = row.getCell(1).getStringCellValue();
        this.phone = row.getCell(2).getStringCellValue();
    }
}
