package com.architecture.admin.models.dto.wordcheck.member;

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
@ExcelFileName(filename = "lang.wordcheck.member.title.list")
public class MemberWordCheckDto implements ExcelDto {

    @ExcelColumnName(headerName = "lang.wordcheck.num")
    private Integer idx;        // 고유번호
    @ExcelColumnName(headerName = "lang.wordcheck.word")
    private String word;        // 금칙어
    @ExcelColumnName(headerName = "lang.wordcheck.changeWord")
    private String changeWord;  // 금칙어 변환값
    private Integer state;      // 상태값
    @ExcelColumnName(headerName = "lang.wordcheck.state")
    private String stateText;   // 상태값 문자변환
    private String stateBg;     // 상태 bg 색상
    private Integer type;       // 타입
    @ExcelColumnName(headerName = "lang.wordcheck.type")
    private String typeText;    // 타입 문자변환
    @ExcelColumnName(headerName = "lang.wordcheck.memo")
    private String memo;        // 메모
    private Integer adminIdx;      // 관리자 idx
    @ExcelColumnName(headerName = "lang.wordcheck.adminId")
    private String adminId;     // 관리자 아이디
    @ExcelColumnName(headerName = "lang.wordcheck.regdate")
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, word, changeWord, stateText, typeText, memo, adminId, regDate);
    }
}