package com.architecture.admin.controllers.qna;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.qna.PartnerQnaDto;
import com.architecture.admin.services.qna.PartnerQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/partner/qna")
public class PartnerQnaController extends BaseController {
    private final PartnerQnaService partnerQnaService;

    /**
     * 파트너사 1:1문의 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 파트너사 1:1문의 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(69);

        // 파트너사 1:1문의 메뉴 리스트
        List<PartnerQnaDto> menuList = partnerQnaService.getMenuList();

        model.addAttribute("menuList", menuList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "qna/partner/list.js.html");

        return "qna/partner/list";
    }

    /**
     * 파트너사 1:1문의 상세
     *
     * @param idx
     * @param model
     * @return 파트너사 1:1문의 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(69);

        // 파트너사 1:1문의 메뉴 리스트
        List<PartnerQnaDto> menuList = partnerQnaService.getMenuList();
        // 매크로 타이틀 가져오기
        List<PartnerQnaDto> macroTitleList = partnerQnaService.getMacroTitleList();

        model.addAttribute("menuList", menuList);
        model.addAttribute("macro", macroTitleList);
        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "qna/partner/view.js.html");

        return "qna/partner/view";
    }
}
