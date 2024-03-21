package com.architecture.admin.api.v1.shopping.product;


import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.models.dto.shopping.product.SearchProductDto;
import com.architecture.admin.services.shopping.product.ProductPartnerService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/product/partner")
public class ProductPartnerV1Controller extends BaseController {

    private final ProductPartnerService productPartnerService;


    /**
     * 파트너사 상품 목록
     *
     * @param searchProductDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchProductDto searchProductDto) {
        // 관리자 접근 권한
        super.adminAccessFail(74);
        //검색어 공백제거
        searchProductDto.setSearchWord(searchProductDto.getSearchWord().trim());
        // list
        List<ProductDto> list = productPartnerService.getListProduct(searchProductDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchProductDto));

        // total count
        int totalCount = searchProductDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.product.partner.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.product.partner.exception.searchFail");

        }
        return displayJson(true, "1000", sMessage, joData);
    }


    /**
     * 파트너 상품 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Long idx) {

        // 관리자 접근 권한
        super.adminAccessFail(70);
        // 상세
        ProductDto dto = productPartnerService.getViewProductPartner(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("productPartner", dto);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (dto == null) {
            sMessage = super.langMessage("lang.product.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }


    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchProductDto searchProductDto) {
        // 관리자 접근 권한
        super.adminAccessFail(6);

        //검색어 공백제거
        searchProductDto.setSearchWord(searchProductDto.getSearchWord().trim());

        Map<String, Object> excelData = productPartnerService.excelDownload(searchProductDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }
}
