package com.architecture.admin.api.v1.wordcheck.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.member.MemberWordCheckDto;
import com.architecture.admin.services.wordcheck.member.MemberWordCheckService;
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
@RequestMapping("/v1/word-check/member")
public class MemberWordCheckV1Controller extends BaseController {

    private final MemberWordCheckService memberWordCheckService;

    /**
     * 회원 금칙어 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto){
        // 관리자 접근 권한
        super.adminAccessFail(18);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<MemberWordCheckDto> list = memberWordCheckService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.wordcheck.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.wordcheck.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 금칙어 엑셀 다운로드
     *
     * @param searchDto
     * @return ModelAndView
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) throws ParseException {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = memberWordCheckService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
