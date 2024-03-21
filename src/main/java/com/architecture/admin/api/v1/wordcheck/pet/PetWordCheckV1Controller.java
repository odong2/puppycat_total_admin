package com.architecture.admin.api.v1.wordcheck.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.pet.PetWordCheckDto;
import com.architecture.admin.services.wordcheck.pet.PetWordCheckService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/word-check/pet")
public class PetWordCheckV1Controller extends BaseController {

    private final PetWordCheckService petWordCheckService;

    /**
     * 반려동물 금칙어 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto){
        // 관리자 접근 권한
        super.adminAccessFail(37);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<PetWordCheckDto> list = petWordCheckService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.wordcheck.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.wordcheck.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 반려동물 금칙어 추가
     *
     * @param petWordCheckDto
     * @return 등록 완료 하였습니다.
     */
    @PostMapping("/regist")
    public ResponseEntity regist(@ModelAttribute("PetWordCheckDto") PetWordCheckDto petWordCheckDto) {
        // 관리자 접근 권한
        super.adminAccessFail(37);
        // 프로필 소개 등록
        int result = petWordCheckService.regist(petWordCheckDto);

        String sMessage;
        // 등록 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.wordcheck.success.regist");
        } else {
            sMessage = super.langMessage("lang.wordcheck.exception.registFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 반려동물 금칙어 삭제
     *
     * @param petWordCheckDto idx
     * @return 삭제 완료 하였습니다.
     */
    @DeleteMapping("/delete")
    public ResponseEntity delete(@ModelAttribute("PetWordCheckDto") PetWordCheckDto petWordCheckDto) {
        // 관리자 접근 권한
        super.adminAccessFail(37);
        // 반려동물 금칙어 삭제
        int result = petWordCheckService.delete(petWordCheckDto);

        String sMessage;
        // 삭제 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.wordcheck.success.delete");
        } else {
            sMessage = super.langMessage("lang.wordcheck.exception.deleteFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }


    /**
     * 반려동물 금칙어 삭제 취소
     *
     * @param petWordCheckDto idx
     * @return 삭제 완료 하였습니다.
     */
    @DeleteMapping("/delete/cancel")
    public ResponseEntity deleteCancel(@ModelAttribute("PetWordCheckDto") PetWordCheckDto petWordCheckDto) {
        // 관리자 접근 권한
        super.adminAccessFail(37);
        // 반려동물 금칙어 삭제 취소
        int result = petWordCheckService.deleteCancel(petWordCheckDto);

        String sMessage;
        // 삭제 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.wordcheck.success.restoration");
        } else {
            sMessage = super.langMessage("lang.wordcheck.exception.restorationFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 반려동물 금칙어 엑셀 다운로드
     *
     * @param searchDto
     * @return ModelAndView
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) throws ParseException {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        Map<String, Object> excelData = petWordCheckService.excelDownload(searchDto);

        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
