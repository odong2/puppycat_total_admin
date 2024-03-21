package com.architecture.admin.controllers.wordcheck.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/word-check/member")
public class MemberWordCheckController extends BaseController {

    /**
     * 닉네임 금칙어 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 닉네임 금칙어 목록 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(18);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "wordcheck/member/list.js.html");

        return "wordcheck/member/list";
    }

    /**
     * 금칙어 등록 레이어
     *
     * @return 금칙어 등록 레이어
     */
    @GetMapping("/layer")
    public String layer() {

        return "wordcheck/member/wordcheckMemberLayer";
    }
}
