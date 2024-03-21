package com.architecture.admin.api.v1.log;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.log.MemberLoginLogService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member/login-log")
public class MemberLoginV1Controller extends BaseController {

    private final MemberLoginLogService memberLoginLogService;

    /**
     * 활동 내역 20개 리스트 by memberUuid
     *
     * @param memberDto memberUuid
     * @return list
     */
    @PostMapping("")
    public ResponseEntity logLastTwentyCasesList(@ModelAttribute("memberDto") MemberDto memberDto) {
        // list
        List<MemberDto> list = memberLoginLogService.getLoginLog(memberDto);
        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.member.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }
}
