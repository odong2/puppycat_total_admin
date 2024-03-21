package com.architecture.admin.controllers.policy;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.policy.PolicyDto;
import com.architecture.admin.services.policy.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/policy")
public class PolicyController extends BaseController {
    private final PolicyService policyService;

    /**
     * 이용약관 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 이용약관 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(16);

        // 공지사항 메뉴 리스트
        PolicyDto policyDto = new PolicyDto();
        List<PolicyDto> menuList = policyService.getPolicyMenuList(policyDto);


        model.addAttribute("menuList", menuList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "policy/list.js.html");

        return "policy/list";
    }

    /**
     * 이용약관 등록
     *
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(16);
        // 공지사항 메뉴 리스트
        PolicyDto policyDto = new PolicyDto();
        List<PolicyDto> menuList = policyService.getPolicyMenuList(policyDto);

        model.addAttribute("menuList", menuList);

        hmImportFile.put("importJs", "policy/regist.js.html");

        return "policy/regist";
    }

    /**
     * 이용약관 상세
     *
     * @param idx
     * @param model
     * @return 이용약관 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(16);
        // 공지사항 메뉴 리스트
        PolicyDto policyDto = new PolicyDto();
        List<PolicyDto> menuList = policyService.getPolicyMenuList(policyDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "policy/view.js.html");

        return "policy/view";
    }
}
