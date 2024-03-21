package com.architecture.admin.controllers.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pet")
public class PetController extends BaseController {
    /**
     * 반려동물 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(33);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "pet/list.js.html");

        return "pet/list";
    }

    /**
     * 반려 동물 상세 레이어
     *
     * @return
     */
    @GetMapping("/view-layer")
    public String imageTagLayer() {

        return "pet/viewLayer";
    }

}
