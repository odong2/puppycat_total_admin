package com.architecture.admin.api.v1.shopping.payments;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.payments.PaymentsDto;
import com.architecture.admin.services.shopping.payments.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/payments")
public class PaymentsV1Controller extends BaseController {

    private final PaymentsService paymentsService;

    /**
     * MP 상품 목록
     * @param searchDto
     * @return
     */
    @GetMapping()
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(81);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // product 조회
        List<PaymentsDto> list = paymentsService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.notice.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.notice.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 결제내역 엑셀 다운로드
     *
     * @param searchDto :  type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(SearchDto searchDto) throws ParseException {

        // 결제내역
        Map<String, Object> excelData = paymentsService.excelDownload(searchDto);

        // 엑셀 데이터 생성
        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
