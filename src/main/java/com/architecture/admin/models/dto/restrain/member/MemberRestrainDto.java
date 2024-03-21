package com.architecture.admin.models.dto.restrain.member;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.restrain.member.title.list")
public class MemberRestrainDto implements ExcelDto {

    private Long idx;            // 고유번호
    private Long memberIdx;      // 회원번호
    private String memberUuid;      // 회원번호
    @ExcelColumnName(headerName = "lang.restrain.member.id")
    @Email
    private String id;              // 아이디
    @ExcelColumnName(headerName = "lang.restrain.member.nick")
    private String nick;            // 닉네임
    private Integer type;           // 제재
    private String typeText;        // 제재상태값 문자변환
    private Integer level;          // 제재레벨
    @ExcelColumnName(headerName = "lang.restrain.member.type")
    private String restrainType;    // 제재 타입
    private Integer typeIdx;        // 제재 타입 idx
    private Integer date;           // 제재 기간
    private Integer dateIdx;        // 제재 기간 idx
    @ExcelColumnName(headerName = "lang.restrain.member.date")
    private String title;           // 제재 기간 문자
    @ExcelColumnName(headerName = "lang.restrain.member.startDate")
    private String startDate;       // 제재 시작일
    private String startDateTz;     // 제재 시작일 타임존
    @ExcelColumnName(headerName = "lang.restrain.member.endDate")
    private String endDate;         // 제재 종료일
    private String endDateTz;       // 제재 종료일 타임존
    private Integer restrainCode;   // 제재 사유 코드
    @ExcelColumnName(headerName = "lang.restrain.member.code")
    private String restrainName;    // 제재 사유
    @ExcelColumnName(headerName = "lang.restrain.member.reason")
    private String reason;          // 제재 사유 ( 기타 )
    @ExcelColumnName(headerName = "lang.restrain.member.adminId")
    private String adminId;         // 관리자 아이디
    private Integer state;          // 상태값
    @ExcelColumnName(headerName = "lang.restrain.member.state")
    private String stateText;       // 상태값 문자변환
    private String stateBg;         // 상태 bg 색상
    private String stateDateText;   // 기간 상태값 문자변환
    private String stateDateBg;     // 기간 상태 bg 색상
    @ExcelColumnName(headerName = "lang.restrain.member.regdate")
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존

    private Long restrainIdx;       // 제재 idx
    private Integer codeIdx;        // 제재 사유 idx
    private String lang;            // 언어


    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(id, nick, restrainType, title, startDate, endDate, restrainName, reason, adminId, stateText, regDate);
    }
}