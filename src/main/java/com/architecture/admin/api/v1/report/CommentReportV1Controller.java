package com.architecture.admin.api.v1.report;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.report.CommentReportDto;
import com.architecture.admin.services.report.CommentReportService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/report/comment")
public class CommentReportV1Controller extends BaseController {

    private final CommentReportService commentReportService;

    /**
     * 댓글 신고 목록
     *
     * @param searchDto 검색조건
     * @return 처리결과
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(11);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<CommentReportDto> listReportComment = commentReportService.getListReportComment(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listReportComment);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.report.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.report.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 신고 댓글 엑셀 다운로드
     *
     * @param searchDto
     * @return ModelAndView
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) throws ParseException {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = commentReportService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
