package com.architecture.admin.controllers.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.services.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController extends BaseController {

    private final MemberService memberService;

    /**
     * 회원 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 회원 목록 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "member/list.js.html");

        return "member/list";
    }

    /**
     * 회원 상세
     *
     * @param memberIdx memberIdx
     * @param model
     * @return 회원 상세페이지
     */
    @GetMapping("/view/{memberIdx}")
    public String view(@PathVariable(name = "memberIdx", required = false) Long memberIdx,
                       Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        String memberUuid = memberService.getMemberUuidByIdx(memberIdx);

        model.addAttribute("memberIdx", memberIdx);
        model.addAttribute("memberUuid", memberUuid);

        hmImportFile.put("importJs", "member/view.js.html");

        return "member/view";
    }

    /**
     * 팔로우/팔로워 랭킹 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping("/rank/list")
    public String rankList(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(51);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "member/rank/list.js.html");

        return "member/rank/list";
    }

    /**
     * 회원 프로필 이미지 등록 레이어
     *
     * @return 회원 프로필 이미지 등록 레이어
     */
    @GetMapping("/image-layer")
    public String imageLayer() {

        return "member/imageLayer";
    }

    /**
     * 회원 랭킹 팔로우/팔로워 리스트 레이어
     *
     * @return
     */
    @GetMapping("/rank/member-layer")
    public String memberLayer() {

        return "member/rank/memberLayer";
    }

}
