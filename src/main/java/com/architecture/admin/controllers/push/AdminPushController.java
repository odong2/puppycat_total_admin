package com.architecture.admin.controllers.push;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.services.push.AdminPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/push/admin")
public class AdminPushController extends BaseController {
    private final AdminPushService adminPushService;

    /**
     * 어드민 푸시 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 어드민 푸시 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(31);

        Boolean workerCheck = adminPushService.getWorkerCheck();

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);
        model.addAttribute("workerState", workerCheck);

        hmImportFile.put("importJs", "push/admin/list.js.html");

        return "push/admin/list";
    }

    /**
     * 푸시 등록
     *
     * @return
     */
    @GetMapping("/regist")
    public String regist() {
        // 관리자 접근 권한
        super.adminAccessFail(31);

        hmImportFile.put("importJs", "push/admin/regist.js.html");

        return "push/admin/regist";
    }

    /**
     * 이미지 등록 레이어
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/imageLayer")
    public String imageLayer() {

        return "push/admin/imageLayer";
    }
}
