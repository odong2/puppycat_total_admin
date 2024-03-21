package com.architecture.admin.controllers.restrain.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.partner.PartnerRestrainDto;
import com.architecture.admin.services.restrain.partner.PartnerRestrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/restrain/partner")
public class PartnerRestrainController extends BaseController {

    private final PartnerRestrainService partnerRestrainService;

    /**
     * 제재 회원 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 제재 회원 목록 페이지
     */
    @GetMapping()
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(78);

        List<PartnerRestrainDto> typeList = partnerRestrainService.getTypeList();

        hmDataSet.put("typeList", typeList);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "restrain/partner/list.js.html");

        return "restrain/partner/list";
    }

    /**
     * 파트너 제재 등록 팝업
     *
     * @return 회원 제재 등록 레이어
     */
    @GetMapping("/regist/{partnerIdx}")
    public String regist(@PathVariable("partnerIdx") Integer partnerIdx) {
        // 관리자 접근 권한
        super.adminAccessFail(78);

        List<PartnerRestrainDto> typeList = partnerRestrainService.getTypeList();

        hmDataSet.put("typeList", typeList);
        hmDataSet.put("idx", partnerIdx);

        hmImportFile.put("importJs", "restrain/partner/regist.js.html");

        return "restrain/partner/regist";
    }

}
