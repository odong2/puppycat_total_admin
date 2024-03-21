package com.architecture.admin.controllers.statistics;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.pet.PetBreedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/statistics/pet")
public class PetStatisticsController extends BaseController {
    private final PetBreedService petBreedService;

    /**
     * 알러지 통계
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 알러지 통계
     */
    @GetMapping("allergy")
    public String allergyStat(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(50);

        // 반려동물 종류 리스트
        List<PetDto> petTypeList = petBreedService.getPetTypeList();

        model.addAttribute("petTypeList", petTypeList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "statistics/pet/allergy.js.html");

        return "statistics/pet/allergy";
    }

    /**
     * 품종 통계
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 품종 통계
     */
    @GetMapping("breed")
    public String breedStat(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(52);

        // 반려동물 종류 리스트
        List<PetDto> petTypeList = petBreedService.getPetTypeList();

        model.addAttribute("petTypeList", petTypeList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "statistics/pet/breed.js.html");

        return "statistics/pet/breed";
    }

}
