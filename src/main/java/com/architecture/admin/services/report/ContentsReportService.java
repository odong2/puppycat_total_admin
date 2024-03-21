package com.architecture.admin.services.report;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.report.ContentsReportDaoSub;
import com.architecture.admin.models.daosub.report.ReportDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.report.ContentsReportDto;
import com.architecture.admin.models.dto.report.ReportCodeDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContentsReportService extends BaseService {

    private final ContentsReportDaoSub contentsReportDaoSub;
    private final ReportDaoSub reportDaoSub;
    private final ExcelData excelData;
    @Value("${report.code.max}")
    private int codeMax;  // 신고사유 코드 MAX

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 게시물 신고 목록
     *
     * @param searchDto 검색조건
     * @return 게시물 신고 리스트
     */
    public JSONObject getListReportContents(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = contentsReportDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        List<ContentsReportDto> list = getList(searchDto);

        // 문자변환
        if (!list.isEmpty()) {
            stateText(list);
        }

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        return joData;
    }

    /**
     * 게시물 신고 목록
     *
     * @param searchDto 검색조건, pagination
     * @return 신고 목록 list
     */
    public List<ContentsReportDto> getList(SearchDto searchDto) {
        List<ContentsReportDto> list = contentsReportDaoSub.getList(searchDto);

        for (ContentsReportDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);
            dto.setContents(contents);
        }

        return list;
    }

    /**
     * 신고사유 목록
     *
     * @return 신고사유 list
     */
    public List<ReportCodeDto> getListReportCode() {
        return reportDaoSub.getListReportCode();
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) throws ParseException {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // list
        List<ContentsReportDto> list = contentsReportDaoSub.getList(searchDto);

        // 상태값 문자 변환
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        for (ContentsReportDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);
            dto.setContents(contents);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, ContentsReportDto.class);
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

    /*****************************************************
     *  SubFunction - Etc
     ****************************************************/
    /**
     * 문자변환 list
     */
    public void stateText(List<ContentsReportDto> list) {

        for (ContentsReportDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     */
    public void stateText(ContentsReportDto dto) {

        // 상태 (0:신고취소, 1:신고)
        if (dto.getState() != null) {
            if (dto.getState() == 0) { // 신고 취소
                dto.setStateText(super.langMessage("lang.report.state.user.delete"));
                dto.setStateBg("badge-success");
            } else if (dto.getState() == 1) { // 신고
                dto.setStateText(super.langMessage("lang.report.state.normal"));
                dto.setStateBg("badge-danger");
            }
        }
        // 신고사유
        if (dto.getReportCodeIdx() != codeMax) {
            dto.setReason(" ");
        }
        // 콘텐츠 상태
        if (dto.getContentsState() != null && dto.getContentsState() == 1) { // 정상
            dto.setContentsStateText(super.langMessage("lang.report.contents.state.normal"));
        } else { // 삭제
            dto.setContentsStateText(super.langMessage("lang.report.contents.state.delete"));
        }
        // 관리자 확인 상태 세팅
        if (ObjectUtils.isEmpty(dto.getAdminChkState()) || (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 0)) { // 미확인
            // 미확인
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.require"));
            dto.setAdminChkBg("badge-danger");
        } else if (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 1) {
            // 확인완료
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.normal"));
            dto.setAdminChkBg("badge-success");
        } else if (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 9) {
            // 제재
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.restrain"));
            dto.setAdminChkBg("badge-warning");
        }

    }

}
