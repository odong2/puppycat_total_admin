package com.architecture.admin.api.v1.shopping.product;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.services.shopping.product.ProductMpService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/product/mp")
public class ProductMpV1controller extends BaseController {

    private final ProductMpService productMpService;

    /**
     * MP 상품 목록
     * @param searchDto
     * @return
     */
    @GetMapping()
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(70);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // product 조회
        List<ProductDto> list = productMpService.getList(searchDto);

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
     * MP 상품 상세
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {

        // 관리자 접근 권한
        super.adminAccessFail(70);
        // 상세
        ProductDto dto = productMpService.getProductMp(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("productMp", dto);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (dto == null) {
            sMessage = super.langMessage("lang.product.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);

    }

}
