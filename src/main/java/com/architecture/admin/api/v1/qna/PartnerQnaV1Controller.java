package com.architecture.admin.api.v1.qna;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.qna.PartnerQnaDto;
import com.architecture.admin.services.qna.PartnerQnaService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/partner/qna")
public class PartnerQnaV1Controller extends BaseController {

    private final PartnerQnaService partnerQnaService;

    /**
     * 1:1문의 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(69);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<PartnerQnaDto> list = partnerQnaService.getListPartnerQna(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.qna.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.qna.exception.searchFail");

        }
        return displayJson(true, "1000", sMessage, joData);
    }


    /**
     * 1:1문의 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(69);
        // 상세
        PartnerQnaDto viewPartnerQna = partnerQnaService.getViewPartnerQna(idx);

        // response object
        JSONObject data = new JSONObject(viewPartnerQna);
        String sMessage = "";

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 매크로 내용 조회
     *
     * @param idx
     * @return
     */
    @GetMapping("/macro/{idx}")
    public ResponseEntity macroContents(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(69);

        // 매크로 내용 가져오기
        String macroCotnetns = partnerQnaService.getMacroContents(idx);

        // response object
        JSONObject data = new JSONObject();
        data.put("contents", macroCotnetns);
        String sMessage = "";

        return displayJson(true, "1000", sMessage, data);
    }


}
