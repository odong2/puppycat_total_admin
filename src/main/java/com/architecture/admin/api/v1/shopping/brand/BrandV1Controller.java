package com.architecture.admin.api.v1.shopping.brand;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import com.architecture.admin.services.shopping.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/brand")
public class BrandV1Controller extends BaseController {

    private final BrandService brandService;

    /**
     * 상품 브랜드 목록
     * @return
     */
    @GetMapping()
    public ResponseEntity lists() {
        // 관리자 접근 권한
        super.adminAccessFail(65);

        // 상품 브랜드 목록
        Map<Integer, List<BrandDto>> brandList = brandService.getBrandList();
        // 브랜드 그룹(초성) 목록
        List<BrandDto> brandGroupList = brandService.getBrandGroupList();

        JSONObject joData = new JSONObject();
        joData.put("list", brandList);
        joData.put("brandGroupList", brandGroupList);

        String sMessage = "";

        return displayJson(true, "1000", sMessage, joData);
    }
}
