package com.architecture.admin.api.v1.cron;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.cron.CronDto;
import com.architecture.admin.services.cron.CronService;
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
@RequestMapping("/v1/cron")
public class CronV1Controller extends BaseController {

    private final CronService cronService;

    /**
     * 크론 리스트
     *
     * @param searchDto state, searchWord, searchType
     * @return ResponseEntity
     */
    @GetMapping("list")
    public ResponseEntity contentsList(@ModelAttribute("param") SearchDto searchDto) {

        // list
        List<CronDto> list = cronService.getCronList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.cron.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.cron.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

}
