package com.architecture.admin.controllers.faq;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.faq.FaqDto;
import com.architecture.admin.services.faq.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/faq")
public class FaqController extends BaseController {
    private final FaqService faqService;

    /**
     * 자주하는 질문 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 자주하는 질문 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(15);

        // 자주하는 질문 메뉴 리스트
        FaqDto faqDto = new FaqDto();
        List<FaqDto> menuList = faqService.getMenuList(faqDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "faq/list.js.html");

        return "faq/list";
    }

    /**
     * 자주하는 질문 등록
     *
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(15);

        // 자주하는 질문 메뉴 리스트
        FaqDto faqDto = new FaqDto();
        List<FaqDto> menuList = faqService.getMenuList(faqDto);

        model.addAttribute("menuList", menuList);

        hmImportFile.put("importJs", "faq/regist.js.html");

        return "faq/regist";
    }

    /**
     * 자주하는 질문 상세
     *
     * @param idx   (sns_board_faq.idx)
     * @param model
     * @return 자주하는 질문 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(15);
        // 자주하는 질문 메뉴 리스트
        FaqDto faqDto = new FaqDto();
        List<FaqDto> menuList = faqService.getMenuList(faqDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("idx", idx);
        hmImportFile.put("importJs", "faq/view.js.html");

        return "faq/view";
    }

}
