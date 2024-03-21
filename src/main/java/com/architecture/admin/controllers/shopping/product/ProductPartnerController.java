package com.architecture.admin.controllers.shopping.product;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import com.architecture.admin.models.dto.shopping.certification.CertificationDto;
import com.architecture.admin.models.dto.shopping.product.SearchProductDto;
import com.architecture.admin.services.partner.PartnerService;
import com.architecture.admin.services.shopping.attributeSet.AttributeSetService;
import com.architecture.admin.services.shopping.certification.CertificationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/shop/product/partner/")
public class ProductPartnerController extends BaseController {

    private final PartnerService partnerService;
    private final CertificationService certificationService;
    private final AttributeSetService attributeSetService;

    /**
     * 파트너 상품 목록
     *
     * @param model
     * @param page
     * @param searchProductDto
     * @return
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchProductDto searchProductDto) {
        // 관리자 접근 권한
        super.adminAccessFail(74);

        // 파트너 법인 리스트
        List<PartnerDto> partnerList = partnerService.getPartnerCompayNameList();

        model.addAttribute("page", page);
        model.addAttribute("search", searchProductDto);
        model.addAttribute("partnerList", partnerList);

        hmImportFile.put("importJs", "shopping/product/partner/list.js.html");

        return "shopping/product/partner/list";
    }

    /**
     * 파트너 상품 상세
     *
     * @param idx
     * @param model
     * @return 파트너 상품 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(74);

        List<CertificationDto> certificationList = certificationService.getCertificationList();
        Map<Integer, List<AttributeSetDto>> attributeSetList = attributeSetService.getAttributeSetList();

        JSONObject jsonData = new JSONObject();
        jsonData.put("certificationList", certificationList);
        jsonData.put("attributeSetList", attributeSetList);

        model.addAttribute("jsonData", jsonData);

        hmImportFile.put("importJs", "shopping/product/partner/view.js.html");

        return "shopping/product/partner/view";
    }
}
