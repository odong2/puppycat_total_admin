package com.architecture.admin.api.v1.restrain.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.member.MemberRestrainDto;
import com.architecture.admin.services.restrain.member.MemberRestrainService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/restrain/member")
public class MemberRestrainV1Controller extends BaseController {

    private final MemberRestrainService memberRestrainService;

    /**
     * 제재 회원 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) throws ParseException {
        // 관리자 접근 권한
        super.adminAccessFail(6);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<MemberRestrainDto> listMember = memberRestrainService.getListMember(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listMember);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.restrain.member.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.restrain.member.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) throws ParseException {
        // 관리자 접근 권한
        super.adminAccessFail(6);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = memberRestrainService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

    /**
     * 회원 제재 목록 by memberUuid
     *
     * @param memberRestrainDto memberUuid
     * @return 검색 완료 하였습니다.
     */
    @PostMapping("/lists/uuid")
    public ResponseEntity idxLists(@ModelAttribute("MemberRestrainDto") MemberRestrainDto memberRestrainDto) throws ParseException {
        // list
        List<MemberRestrainDto> listMember = memberRestrainService.getListMemberByMemberIdx(memberRestrainDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listMember);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.restrain.member.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 제재 내역 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable("idx") Long idx) {

        MemberRestrainDto viewRestrain = memberRestrainService.getView(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("restrain", viewRestrain);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = super.langMessage("lang.restrain.member.success.search");

        return displayJson(true, "1000", sMessage, data);
    }


}
