package com.architecture.admin.models.dto.excel;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelReadDto {

    void fillUpFromRow(Row row);

}
