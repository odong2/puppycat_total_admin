package com.architecture.admin.api.v1.chat;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.block.BlockMemberDto;
import com.architecture.admin.models.dto.chat.ChatMessageReportDto;
import com.architecture.admin.services.block.BlockMemberService;
import com.architecture.admin.services.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/chat/report")
public class ChatReportV1Controller extends BaseController {

    private final ChatService chatService;

    /**
     * 채팅 신고 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(76);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<ChatMessageReportDto> list = chatService.getChatReportList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.block.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 채팅 신고 리스트
     *
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable("idx") Integer idx) throws JsonProcessingException {
        // 관리자 접근 권한
        super.adminAccessFail(76);
        List<ChatMessageReportDto> list = null;
        JSONObject joData = new JSONObject();

        if (idx != null && idx > 0) {
            // list
            list = chatService.getChatReportView(idx);

            if (list.get(0) != null) {
                //채팅 로그
                Object chatLog = chatService.getRoomLog(list.get(0));
                joData.put("log", chatLog);
            }
        }

        joData.put("list", list);

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.block.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }
}
