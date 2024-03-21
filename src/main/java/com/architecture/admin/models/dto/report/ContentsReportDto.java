package com.architecture.admin.models.dto.report;

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
@ExcelFileName(filename = "lang.report.contents.list")
public class ContentsReportDto implements ExcelDto {

    /**
     * sns_contents_report
     */
    @ExcelColumnName(headerName = "lang.report.num")
    private Long idx; // 일련번호
    private Long memberIdx; // 회원 idx
    private String memberUuid; // 회원 uuid
    @ExcelColumnName(headerName = "lang.report.reporter.id")
    private String id; // 회원 id
    @ExcelColumnName(headerName = "lang.report.reporter.nick")
    private String nick; // 회원 닉네임
    private String reportMemberUuid; // 신고된 회원 uuid
    @ExcelColumnName(headerName = "lang.report.reported.id")
    private String reportMemberId; // 신고된 회원 id
    private Long reportMemberIdx; // 신고된 회원 idx
    @ExcelColumnName(headerName = "lang.report.reported.nick")
    private String reportMemberNick; // 신고된 회원 닉네임
    private Integer reportMemberState; // 신고된 회원 상태
    @ExcelColumnName(headerName = "lang.report.contents")
    private String contents; // 콘텐츠 내용
    private Long contentsIdx; // 콘텐츠 idx
    private Integer reportCode; // 신고사유 코드
    private Integer contentsState; // 콘텐츠 상태 (0:삭제, 1:정상, 9:관리자제재)
    @ExcelColumnName(headerName = "lang.report.contents.state")
    private String contentsStateText; // 콘텐츠 상태 문자변환
    private Integer contentsImgState; // 콘텐츠 이미지 상태 (0:삭제, 1:정상, 9:관리자제재)
    private Integer memberRestrainState; // 회원 제재 상태 (0:삭제, 1:정상, 9:관리자제재)
    private Integer state; // 신고 상태 (0:취소, 1:신고, 2:관리자삭제)
    @ExcelColumnName(headerName = "lang.report.state")
    private String stateText; // 신고 상태 문자변환
    private String stateBg; // 신고 상태 bg 색상
    @ExcelColumnName(headerName = "lang.report.regdate")
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존

    /**
     * sns_report_code
     */
    private Integer reportCodeIdx; // 신고사유 idx
    @ExcelColumnName(headerName = "lang.report.reason")
    private String name; // 신고사유
    @ExcelColumnName(headerName = "lang.report.detail.reason")
    private String reason; // 신고 상세사유

    /**
     * sns_contents_report_admin_log
     */
    private Integer adminIdx;

    // sql
    private Long insertedIdx;

    // etc
    private List<Long> idxList; // idx 리스트
    private Integer adminChkState; // 관리자 확인 상태 (0:미확인, 1:확인완료, 9:제재완료)
    @ExcelColumnName(headerName = "lang.report.state.admin.check.state")
    private String adminChkStateText; // 관리자 확인 상태 문자변환
    private String adminChkBg; // 관리자 확인 상태 bg 색상
    @ExcelColumnName(headerName = "lang.report.state.admin.check.date")
    private String adminChkDate; // 관리자 처리일

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, id, nick, reportMemberId, reportMemberNick, contents, contentsStateText, stateText, regDate, name, reason, adminChkStateText, adminChkDate);
    }
}

