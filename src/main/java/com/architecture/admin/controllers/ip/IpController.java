package com.architecture.admin.controllers.ip;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ip")
public class IpController extends BaseController {

    /**
     * 관리자 IP 목록 조회
     *
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping()
    public String getAdminIpList(Model model,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @ModelAttribute SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "ip/list.js.html");

        return "ip/list";
    }

    /**
     * 관리자 IP 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/{idx}")
    public String getViewAdminIp(@PathVariable(required = false) Long idx, Model model) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        model.addAttribute("idx", idx);

        hmImportFile.put("importJs", "ip/view.js.html");

        return "ip/view";
    }

    /**
     * 관리자 IP 등록
     */
    @GetMapping("/register")
    public String registerAdminIp() {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // set view data
        hmImportFile.put("importJs", "ip/register.js.html");

        return "ip/register";
    }

    /**
     * 관리자 IP 수정
     */
    @GetMapping("/modify/{idx}")
    public String modifyAdminIp(@PathVariable(required = false) Long idx) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // set view data
        hmImportFile.put("importJs", "ip/modify.js.html");

        return "ip/modify";
    }
}
