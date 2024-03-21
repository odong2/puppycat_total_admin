package com.architecture.admin.api.v1.log;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.log.MemberAccessLogDto;
import com.architecture.admin.services.log.MemberAccessLogService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member/access-log")
public class MemberAccessLogV1Controller extends BaseController {

    private final MemberAccessLogService memberAccessLogService;

    /**
     * 활동 내역 20개 리스트 by memberUuid
     *
     * @param memberAccessLogDto memberUuid
     * @return list
     */
    @PostMapping("")
    public ResponseEntity logLastTwentyCasesList(@ModelAttribute("MemberActionLogDto") MemberAccessLogDto memberAccessLogDto) {
        // list
        List<MemberAccessLogDto> list = memberAccessLogService.getMemberAccessLogLastTwentyCasesListByMemberUuid(memberAccessLogDto);
        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.member.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }
}
