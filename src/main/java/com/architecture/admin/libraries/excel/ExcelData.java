package com.architecture.admin.libraries.excel;

import com.architecture.admin.libraries.DateLibrary;
import com.architecture.admin.models.dto.excel.ExcelDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExcelData { //

    private final MessageSource messageSource;
    // 시간 라이브러리 참조
    @Autowired
    protected DateLibrary dateLibrary;

    // 모델 객체에 담을 형태로 엑셀 데이터 생성
    public Map<String, Object> createExcelData(List<? extends ExcelDto> data, Class<?> target) {
        // 현재 시간 UTC 가져오기
        String nowDate = dateLibrary.getDatetime();
        // 로컬 시간으로 변경
        nowDate = dateLibrary.utcToLocalTime(nowDate);

        Map<String, Object> excelData = new HashMap<>();
        excelData.put("filename", createFileName(target)+"_"+nowDate);
        excelData.put("head", createHeaderName(target));
        excelData.put("body", createBodyData(data));
        return excelData;
    }

    // @ExcelColumnName에서 헤더 이름 리스트 생성
    private List<String> createHeaderName(Class<?> header) {
        List<String> headData = new ArrayList<>();
        for (Field field : header.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumnName.class)) {
                // 다국어 적용전 properties key 값(lang.member.nick 등)
                String headerName = field.getAnnotation(ExcelColumnName.class).headerName();
                if (headerName.equals("")) {
                    headData.add(field.getName());
                } else {
                    // 다국어 적용
                    String name = langMessage(headerName);
                    headData.add(name);
                }
            }
        }
        return headData;
    }

    // @ExcelFileName 에서 엑셀 파일 이름 생성
    private String createFileName(Class<?> file) throws RuntimeException {
        if (file.isAnnotationPresent(ExcelFileName.class)) {

            String fileNameCode = file.getAnnotation(ExcelFileName.class).filename();
            String fileName = langMessage(fileNameCode);

            // 한글 및 공백 깨짐 방지를 위해 Encoding 지정
            byte[] fileNameBytes = StringUtils.getBytesUtf8(fileName);
            String filename = StringUtils.newStringUtf8(fileNameBytes);

            return filename.equals("") ? file.getSimpleName() : filename;
        }
        throw new RuntimeException("excel filename not exist");
    }

    // 데이터 리스트 형태로 가공
    private List<List<Object>> createBodyData(List<? extends ExcelDto> dataList) {
        List<List<Object>> bodyData = new ArrayList<>();
        dataList.forEach(v -> bodyData.add(v.mapToList()));
        return bodyData;
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
