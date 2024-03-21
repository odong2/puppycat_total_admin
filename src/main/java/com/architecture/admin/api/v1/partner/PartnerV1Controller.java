package com.architecture.admin.api.v1.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.services.partner.PartnerService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/partner")
public class PartnerV1Controller extends BaseController {

    private final PartnerService partnerService;

    /**
     * 파트너 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("")
    public ResponseEntity<String> partnerList(SearchDto searchDto) {

        // 레벨 체크
        super.adminAccessFail(72);

        // 파트너 리스트
        List<PartnerDto> partnerList = partnerService.getPartnerList(searchDto);

        JSONObject data = new JSONObject();
        data.put("list", partnerList);
        data.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.common.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.pet.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 관리자 허용 IP 리스트 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView partnerListExcelDownload(@ModelAttribute("param") SearchDto searchDto) {

        // 레벨 체크
        super.adminAccessFail(72);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = partnerService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 아이디 체크
     *
     * @param id
     * @return
     */
    @GetMapping("/check/id")
    public ResponseEntity checkId(@RequestParam String id) {
        // 아이디 체크
        partnerService.checkId(id);

        String message = super.langMessage("lang.admin.success.search");

        return displayJson(true, "1000", message);
    }

    /**
     * 사업자 번호 체크
     *
     * @param businessNumber
     * @return
     */
    @GetMapping("/check/business-number")
    public ResponseEntity checkBusinessNumber(@RequestParam String businessNumber) {
        // 사업자 번호 체크
        partnerService.checkBusinessNumber(businessNumber);

        String message = super.langMessage("lang.admin.success.search");

        return displayJson(true, "1000", message);
    }


}
