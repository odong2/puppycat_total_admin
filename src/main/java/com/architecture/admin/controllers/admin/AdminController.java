package com.architecture.admin.controllers.admin;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    /**
     * 관리자 목록
     *
     * @param searchDto
     * @return 관리자 목록
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(3);

        hmImportFile.put("importJs", "admin/list.js.html");

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        return "admin/list";
    }

    /**
     * 관리자 상세
     *
     * @param idx   (sns_admin.idx)
     * @param model
     * @return 관리자 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(3);

        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "admin/view.js.html");

        return "admin/view";
    }


    /**
     * 관리자 내 정보 수정
     *
     * @param idx   (admin.idx)
     * @param model
     * @return
     */
    @GetMapping("/mypage/{idx}")
    public String modifyMyPage(@PathVariable(required = false) int idx, Model model) {

        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "admin/mypage.js.html");

        return "admin/mypage";
    }

    /**
     * 관리자 비밀번호 수정
     *
     * @param idx   (admin.idx)
     * @param model
     * @return
     */
    @GetMapping("/mypage/password/{idx}")
    public String modifyPassword(@PathVariable(required = false) Long idx, Model model) {

        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "admin/password.js.html");

        return "admin/password";
    }
}
