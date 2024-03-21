package com.architecture.admin.api.v1.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.BreedDto;
import com.architecture.admin.services.pet.PetBreedService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/pet/breed")
public class PetBreedV1Controller extends BaseController {
    private final PetBreedService petBreedService;

    /**
     * 품종 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(22);

        if (searchDto.getType() == null) {
            searchDto.setType(2);
        }

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<BreedDto> list = petBreedService.getList(searchDto);

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
     * 품종 기타 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/etc/list")
    public ResponseEntity etcLists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(35);

        // 페이지에 노출 될 개수
        if (searchDto.getLimit() != 10) {
            searchDto.setRecordSize(searchDto.getLimit());
        }

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<BreedDto> list = petBreedService.getEtcList(searchDto);

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
     * 품종 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView breedExcelDownload(SearchDto searchDto) {

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = petBreedService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 기타 품종 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/etc/excel")
    public ModelAndView etcBreedExcelDownload(SearchDto searchDto) {

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = petBreedService.etcExcelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

}
