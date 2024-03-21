package com.architecture.admin.libraries.excel;

import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ExcelLibrary {

    private final MessageSource messageSource;
    
    /**
     * Workbook 생성 메서드
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Workbook createWorkBook(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Workbook workbook;
        switch (extension) {
            case "xlsx" -> workbook = new XSSFWorkbook(file.getInputStream());
            case "xls" -> workbook = new HSSFWorkbook(file.getInputStream());
            default -> throw new IOException(); // 확장자가 excel이 아닌 경우
        }
        return workbook;
    }

    /**
     * workBook close 메서드.
     * close 하지 않으면 outOFMemory 발생할 가능성 존재
     *
     * @param workbook
     */
    public void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 엑셀 파일 읽기 & 파싱
     *
     * @param file      업로드한 엑셀 파일
     * @param target    매핑할 타겟 class
     * @return List<T>  매핑된 자바 Object List
     */
    public <T> List<T> parseExcelToObject(MultipartFile file, Class<T> target) {

        Sheet sheet = null;

        try {
            // 엑셀 파일 읽기
            Workbook workbook = createWorkBook(file);
            // 첫번째 시트 조회
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            // 엑셀 파일을 읽지 못하였습니다.
            throw new CustomException(CustomError.EXCEL_READ_ERROR);
        }

        // 헤더 & 바디 파싱
        parseHeader(sheet, target);
        return parseBody(sheet, target);
    }

    /**
     * 헤더 파싱 & 검증
     *
     * @param sheet     시트 번호
     * @param target    타겟 class
     * @param <T>       매핑될 class
     */
    private <T> void parseHeader(Sheet sheet, Class<T> target) {
        Set<String> excelHeaders = new HashSet<>();
        Set<String> classHeaders = new HashSet<>();

        // 헤더 첫 번째 행 고정
        int headerStartRowToParse = 0;

        // 헤더 배열 저장
        sheet.getRow(headerStartRowToParse).cellIterator()
                .forEachRemaining(e -> excelHeaders.add(e.getStringCellValue()));

        // 지정된 헤더 배열 forEach
        Arrays.stream(target.getDeclaredFields())
                .filter(e -> e.isAnnotationPresent(ExcelReadColumnName.class))
                .forEach(e -> {
                    String headerName = e.getAnnotation(ExcelReadColumnName.class).headerName();
                    if (headerName.isEmpty()) {
                        classHeaders.add(headerName);
                    } else {
                        // 다국어 적용
                        classHeaders.add(langMessage(headerName));
                    }
                });

        // 잘못된 헤더값
        if (!excelHeaders.containsAll(classHeaders)) {
            // 엑셀 양식이 올바르지 않습니다.
            throw new CustomException(CustomError.EXCEL_SAMPLE_ERROR);
        }
    }

    /**
     * 바디 파싱 & 자바 객체로 변환
     *
     * @param sheet     시트 번호
     * @param target    타겟 class
     * @return List<T>  매핑된 자바 Object List
     * @param <T>       매핑될 class
     */
    private <T> List<T> parseBody(Sheet sheet, Class<T> target) {
        List<T> objects = new ArrayList<>();

        try {

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum == 0) {
                // 엑셀에 입력된 데이터가 없습니다.
                throw new CustomException(CustomError.EXCEL_DATA_EMPTY);
            }

            for(int i = 1; i <= sheet.getLastRowNum(); i++) {
                T object = target.getDeclaredConstructor().newInstance();
                target.getMethod("fillUpFromRow", Row.class).invoke(object, sheet.getRow(i));
                objects.add(object);
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // 엑셀을 읽던 중 에러가 발생했습니다.
            throw new CustomException(CustomError.EXCEL_PARSING_ERROR);
        }

        return objects;
    }

    /*****************************************************
     * Language 값
     ****************************************************/
    public String langMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String langMessage(String code, @Nullable Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
    
}
