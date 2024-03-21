package com.architecture.admin.controllers.pet;

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
@RequestMapping("/pet/breed")
public class PetBreedController extends BaseController {
    private final PetBreedService petBreedService;

    /**
     * 반려동물 품종 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 반려동물 품종 목록 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(22);

        // 반려동물 종류 리스트
        List<PetDto> petTypeList = petBreedService.getPetTypeList();

        model.addAttribute("petTypeList", petTypeList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "pet/breed/list.js.html");

        return "pet/breed/list";
    }

    /**
     * 품종 등록 레이어
     *
     * @return 품종 등록 레이어
     */
    @GetMapping("/layer")
    public String layer(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(22);

        // 반려동물 종류 리스트
        List<PetDto> petTypeList = petBreedService.getPetTypeList();

        model.addAttribute("petTypeList", petTypeList);

        return "pet/breed/petBreedLayer";
    }

    /**
     * 품종 정렬 변경 레이어
     *
     * @return 정렬 변경 레이어
     */
    @GetMapping("/sortLayer")
    public String sortLayer(Model model,
                            @RequestParam(value = "idx", defaultValue = "1") long idx) {
        // 관리자 접근 권한
        super.adminAccessFail(22);

        // 저장된 품종 정보 가져오기
        PetDto breedInfo = petBreedService.getBreedInfo(idx);

        model.addAttribute("breedInfo", breedInfo);

        return "pet/breed/petBreedSortLayer";
    }

    /**
     * 품종 수정 레이어
     *
     * @return 품종 수정 레이어
     */
    @GetMapping("/modifyLayer")
    public String modifyLayer(Model model,
                            @RequestParam(value = "idx", defaultValue = "1") long idx) {
        // 관리자 접근 권한
        super.adminAccessFail(22);

        // 저장된 품종 정보 가져오기
        PetDto breedInfo = petBreedService.getBreedInfo(idx);

        model.addAttribute("breedInfo", breedInfo);

        return "pet/breed/petBreedModifyLayer";
    }
}