package com.architecture.admin.controllers.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.HealthDto;
import com.architecture.admin.services.pet.PetHealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/pet/health")
public class PetHealthController extends BaseController {

    private final PetHealthService petHealthService;
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(60);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "pet/health/list.js.html");

        return "pet/health/list";
    }

    /**
     * 등록 레이어
     *
     * @return 품종 등록 레이어
     */
    @GetMapping("/layer")
    public String layer() {
        // 관리자 접근 권한
        super.adminAccessFail(60);

        return "pet/health/petHealthLayer";
    }

    /**
     * 정렬 변경 레이어
     *
     * @return 품종 등록 레이어
     */
    @GetMapping("/sortLayer")
    public String sortLayer(Model model,
                            @RequestParam(value = "idx", defaultValue = "1") long idx) {
        // 관리자 접근 권한
        super.adminAccessFail(60);

        // 저장된 품종 정보 가져오기
        HealthDto healthInfo = petHealthService.getHealthInfo(idx);

        model.addAttribute("healthInfo", healthInfo);

        return "pet/health/petHealthSortLayer";
    }

    /**
     * 건강 수정 레이어
     *
     * @return
     */
    @GetMapping("/modifyLayer")
    public String modifyLayer(Model model,
                              @RequestParam(value = "idx", defaultValue = "1") long idx) {
        // 관리자 접근 권한
        super.adminAccessFail(60);

        // 저장된 품종 정보 가져오기
        HealthDto healthInfo = petHealthService.getHealthInfo(idx);

        model.addAttribute("healthInfo", healthInfo);

        return "pet/health/petHealthModifyLayer";
    }
}
