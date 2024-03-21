package com.architecture.admin.controllers.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.OutMemberDto;
import com.architecture.admin.services.member.OutMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/out/members")
public class OutMemberController extends BaseController {

    private final OutMemberService outMemberService;

    /**
     * 탈퇴 회원 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public String outLists(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(7);

        // 탈퇴 사유 리스트
        List<OutMemberDto> outCodeList = outMemberService.getOutCodeList();

        hmDataSet.put("outCodeList", outCodeList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "member/out/list.js.html");

        return "member/out/list";
    }

}
