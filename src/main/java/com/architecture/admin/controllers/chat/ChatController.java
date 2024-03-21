package com.architecture.admin.controllers.chat;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.admin.AdminService;
import com.architecture.admin.services.contents.ContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController {

    private final ContentsService contentsService;
    private final AdminService adminService;

    /**
     * 컨텐츠 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 회원 컨텐츠 페이지
     */
    @GetMapping("/report/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(76);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "chat/report_list.js.html");

        return "chat/report_list";
    }

    /**
     * 채팅 신고 상세
     *
     * @param idx
     * @return 회원 컨텐츠 상세 수정 페이지
     */
    @GetMapping("/report/view/{idx}")
    public String detail(Model model, @PathVariable("idx") Long idx){
        // 관리자 접근 권한
        super.adminAccessFail(76);

        hmDataSet.put("idx", idx);
        model.addAttribute("idx", idx);
        // view pages
        hmImportFile.put("importJs", "chat/report_view.js.html");
        return "chat/report_view";
    }

    @GetMapping("/member-layer")
    public String memberLayer() {

        return "contents/memberLayer";
    }
}
