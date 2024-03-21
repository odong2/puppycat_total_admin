package com.architecture.admin.controllers.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.services.pet.PetAllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/pet/allergy")
public class PetAllergyController extends BaseController {

    private final PetAllergyService petAllergyService;
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(62);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "pet/allergy/list.js.html");

        return "pet/allergy/list";
    }

    /**
     * 등록 레이어
     *
     * @return 품종 등록 레이어
     */
    @GetMapping("/layer")
    public String layer() {
        // 관리자 접근 권한
        super.adminAccessFail(62);

        return "pet/allergy/petAllergyLayer";
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
        super.adminAccessFail(62);

        // 저장된 알러지 정보 가져오기
        AllergyDto allergyInfo = petAllergyService.getAllergyInfo(idx);

        model.addAttribute("allergyInfo", allergyInfo);

        return "pet/allergy/petAllergySortLayer";
    }

    /**
     * 알러지 수정 레이어
     *
     * @return
     */
    @GetMapping("/modifyLayer")
    public String modifyLayer(Model model,
                            @RequestParam(value = "idx", defaultValue = "1") long idx) {
        // 관리자 접근 권한
        super.adminAccessFail(62);

        // 저장된 알러지 정보 가져오기
        AllergyDto allergyInfo = petAllergyService.getAllergyInfo(idx);

        model.addAttribute("allergyInfo", allergyInfo);

        return "pet/allergy/petAllergyModifyLayer";
    }
}
