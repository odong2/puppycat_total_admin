package com.architecture.admin.api.v1.restrain.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.partner.PartnerRestrainDto;
import com.architecture.admin.services.restrain.partner.PartnerRestrainService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/restrain/partner")
public class PartnerRestrainV1Controller extends BaseController {

    private final PartnerRestrainService partnerRestrainService;

    /**
     * 제재 파트너 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity getList(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(78);

        // 검색어 공백 제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        List<PartnerRestrainDto> listMember = partnerRestrainService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listMember);
        joData.put("params", new JSONObject(searchDto));

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.common.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(78);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = partnerRestrainService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
