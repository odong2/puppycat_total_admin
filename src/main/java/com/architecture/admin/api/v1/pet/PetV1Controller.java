package com.architecture.admin.api.v1.pet;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.pet.PetService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/pet")
public class PetV1Controller extends BaseController {

    private final PetService petService;

    /**
     * 반려동물 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getPetList(SearchDto searchDto) {
        super.adminAccessFail(33);

        List<PetDto> petList = petService.getPetList(searchDto);

        JSONObject data = new JSONObject();
        data.put("list", petList);
        data.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.contents.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 반려동물 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/{idx}/members/{memberIdx}")
    public ResponseEntity getPetDetailList(@PathVariable Long idx,
                                           @PathVariable Long memberIdx) {
        SearchDto searchDto = new SearchDto();
        searchDto.setPetIdx(idx);
        // TODO : memberUuid 로 변경 필요
//        searchDto.setMemberIdx(memberIdx);

        List<PetDto> petList = petService.getDetailPetList(searchDto);

        JSONObject data = new JSONObject();
        data.put("list", petList);

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 이미지 확대
     *
     * @param petDto : originUrl, imgWidth, imgHeight
     * @return
     */
    @GetMapping("/image")
    public ResponseEntity<String> getPetBigImg(@ModelAttribute PetDto petDto) {
        String imgUrl = petService.getPetBigImg(petDto);

        JSONObject data = new JSONObject();
        data.put("imgUrl", imgUrl);

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(@ModelAttribute("param") SearchDto searchDto) {

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = petService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }
}
