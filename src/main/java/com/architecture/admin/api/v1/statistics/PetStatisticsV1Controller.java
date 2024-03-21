package com.architecture.admin.api.v1.statistics;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.statistics.pet.PetStatisticsService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/statistics/pet")
public class PetStatisticsV1Controller extends BaseController {

    private final PetStatisticsService petStatisticsService;

    /**
     * allergy 통계
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("allergy")
    public ResponseEntity allergyStat(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(50);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<PetDto> list = petStatisticsService.getAllergy(searchDto);
        List<PetDto> detailLists = petStatisticsService.getBreedAllergy(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("detailLists", detailLists);
        joData.put("params", new JSONObject(searchDto));

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.push.admin.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * breed 통계
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("breed")
    public ResponseEntity breedStat(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(52);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<PetDto> list = petStatisticsService.getBreedStat(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.push.admin.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

}
