package com.architecture.admin.controllers.restrain.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.member.MemberRestrainDto;
import com.architecture.admin.services.restrain.member.MemberRestrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/restrain/members")
public class MemberRestrainController extends BaseController {
    private final MemberRestrainService memberRestrainService;

    /**
     * 제재 회원 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 제재 회원 목록 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(6);

        List<MemberRestrainDto> codeList = memberRestrainService.getCodeList();
        List<MemberRestrainDto> typeList = memberRestrainService.getTypeList();
        List<MemberRestrainDto> dateList = memberRestrainService.getDateList();

        hmDataSet.put("dateList", dateList);
        hmDataSet.put("typeList", typeList);
        hmDataSet.put("codeList", codeList);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "restrain/member/list.js.html");

        return "restrain/member/list";
    }

    /**
     * 회원 제재 신청 레이어
     *
     * @return 회원 제재 신청 레이어
     */
    @GetMapping("/apply/{memberUuid}")
    public String apply(@PathVariable("memberUuid") String memberUuid) {
        List<MemberRestrainDto> codeList = memberRestrainService.getCodeList();
        List<MemberRestrainDto> typeList = memberRestrainService.getTypeList();
        List<MemberRestrainDto> dateList = memberRestrainService.getDateList();

        hmDataSet.put("dateList", dateList);
        hmDataSet.put("typeList", typeList);
        hmDataSet.put("codeList", codeList);
        hmDataSet.put("memberUuid", memberUuid);

        hmImportFile.put("importJs", "restrain/member/apply.js.html");

        return "restrain/member/apply";
    }


    /**
     * 회원 제재 등록 레이어
     *
     * @return 회원 제재 등록 레이어
     */
    @GetMapping("/regist/{memberUuid}")
    public String regist(@PathVariable("memberUuid") String memberUuid) {
        List<MemberRestrainDto> codeList = memberRestrainService.getCodeList();
        List<MemberRestrainDto> typeList = memberRestrainService.getTypeList();
        List<MemberRestrainDto> dateList = memberRestrainService.getDateList();

        hmDataSet.put("dateList", dateList);
        hmDataSet.put("typeList", typeList);
        hmDataSet.put("codeList", codeList);
        hmDataSet.put("memberUuid", memberUuid);

        hmImportFile.put("importJs", "restrain/member/regist.js.html");

        return "restrain/member/regist";
    }

    @GetMapping("/modify/{idx}")
    public String modify(@PathVariable("idx") Integer idx) {
        List<MemberRestrainDto> codeList = memberRestrainService.getCodeList();
        List<MemberRestrainDto> typeList = memberRestrainService.getTypeList();
        List<MemberRestrainDto> dateList = memberRestrainService.getDateList();

        hmDataSet.put("dateList", dateList);
        hmDataSet.put("typeList", typeList);
        hmDataSet.put("codeList", codeList);
        hmDataSet.put("idx", idx);
        // view pages
        hmImportFile.put("importJs", "restrain/member/modify.js.html");

        return "restrain/member/modify";

    }
}
