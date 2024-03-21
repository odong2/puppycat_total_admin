package com.architecture.admin.api.v1.notice;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.PartnerNoticeDto;
import com.architecture.admin.services.notice.PartnerNoticeService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/partner/notice")
public class PartnerNoticeV1Controller extends BaseController {

    private final PartnerNoticeService partnerNoticeService;

    /**
     * 공지사항 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(63);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<PartnerNoticeDto> list = partnerNoticeService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.notice.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.notice.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 공지사항 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(63);
        // 상세
        PartnerNoticeDto viewNotice = partnerNoticeService.getViewNotice(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("notice", viewNotice);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (viewNotice == null) {
            sMessage = super.langMessage("lang.notice.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }

}
