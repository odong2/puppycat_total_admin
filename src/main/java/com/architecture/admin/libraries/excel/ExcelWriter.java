package com.architecture.admin.libraries.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExcelWriter {

    private final Workbook workbook;
    private final Map<String, Object> data;
    private final HttpServletResponse response;

    // 생성자
    public ExcelWriter(Workbook workbook, Map<String, Object> data, HttpServletResponse response) {
        this.workbook = workbook;
        this.data = data;
        this.response = response;
    }

    // 엑셀 파일 생성
    public void create() throws IOException {
        setFileName(response, mapToFileName());

        Sheet sheet = workbook.createSheet();

        createHead(sheet, mapToHeadList());

        createBody(sheet, mapToBodyList());
    }

    // 모델 객체에서 파일 이름 꺼내기
    private String mapToFileName() {
        return (String) data.get("filename");
    }

    // 모델 객체에서 헤더 이름 리스트 꺼내기
    @SuppressWarnings("unchecked")
    private List<Object> mapToHeadList() {
        return (List<Object>) data.get("head");
    }

    // 모델 객체에서 바디 데이터 리스트 꺼내기
    @SuppressWarnings("unchecked")
    private List<List<Object>> mapToBodyList() {
        return (List<List<Object>>) data.get("body");
    }

    // 파일 이름 지정
    private void setFileName(HttpServletResponse response, String fileName) throws IOException {
        String outputFileName = new String(getFileExtension(fileName).getBytes("KSC5601"), StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + outputFileName + "\"");
    }

    // 넘어온 뷰에 따라서 확장자 결정
    private String getFileExtension(String fileName) {
        if (workbook instanceof XSSFWorkbook) {
            fileName += ".xlsx";
        }
        if (workbook instanceof SXSSFWorkbook) {
            fileName += ".xlsx";
        }
        if (workbook instanceof HSSFWorkbook) {
            fileName += ".xls";
        }

        return fileName;
    }

    // 엑셀 헤더 생성
    private void createHead(Sheet sheet, List<Object> headList) {
        createRow(sheet, headList, 0, "header");
    }

    // 엑셀 바디 생성
    private void createBody(Sheet sheet, List<List<Object>> bodyList) {
        int rowSize = bodyList.size();
        for (int i = 0; i < rowSize; i++) {
            createRow(sheet, bodyList.get(i), i + 1, "body");
        }
    }

    // 행 생성
    private void createRow(Sheet sheet, List<Object> cellList, int rowNum, String type) {
        int size = cellList.size();
        Row row = sheet.createRow(rowNum);

        if (type.equals("header")) {
            // 볼드 처리 & 가운데 정렬
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < size; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue((String) cellList.get(i));
                cell.setCellStyle(style);
            }
        } else if (type.equals("body")) {
            for (int i = 0; i < size; i++) {
                Object object = cellList.get(i);
                if (object instanceof String) {
                    row.createCell(i).setCellValue((String) object);
                } else if (object instanceof Long longValue) {
                    row.createCell(i).setCellValue(longValue);
                } else if (object instanceof Integer integerValue) {
                    row.createCell(i).setCellValue(integerValue);
                }
            }
        }

    }
}