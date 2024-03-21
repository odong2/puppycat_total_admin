package com.architecture.admin.api.v1.report;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.services.report.ContentsReportService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/report/contents")
public class ContentsReportV1Controller extends BaseController {

    private final ContentsReportService contentsReportService;

    /**
     * 게시물 신고 목록
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
        JSONObject joData = contentsReportService.getListReportContents(searchDto);

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
     * 신고 콘텐츠 엑셀 다운로드
     *
     * @param searchDto
     * @return ModelAndView
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) throws ParseException {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = contentsReportService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
