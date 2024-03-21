package com.architecture.admin.api.v1.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.services.pet.PetAllergyService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/pet/allergy")
public class PetAllergyV1Controller extends BaseController {

    private final PetAllergyService petAllergyService;

    /**
     * 알러지 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(62);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<AllergyDto> list = petAllergyService.getAllergyList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.pet.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.pet.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 알러지 전체 리스트 조회
     * @return
     */
    @GetMapping("/allList")
    public ResponseEntity allLists() {

        // list
        List<AllergyDto> list = petAllergyService.getAllAllergyList();

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.pet.success.search");
        if (list.size() < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.pet.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 알러지 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView breedExcelDownload(SearchDto searchDto) {

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = petAllergyService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }
}
