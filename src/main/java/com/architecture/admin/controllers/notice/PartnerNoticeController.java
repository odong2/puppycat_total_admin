package com.architecture.admin.controllers.notice;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.PartnerNoticeDto;
import com.architecture.admin.services.notice.PartnerNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/partner/notice")
public class PartnerNoticeController extends BaseController {
    private final PartnerNoticeService partnerNoticeService;

    /**
     * 파트너사 공지사항 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 파트너사 공지사항 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(63);

        // 파트너사 공지사항 메뉴 리스트
        PartnerNoticeDto partnerNoticeDto = new PartnerNoticeDto();
        List<PartnerNoticeDto> menuList = partnerNoticeService.getMenuList(partnerNoticeDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "notice/partner/list.js.html");

        return "notice/partner/list";
    }

    /**
     * 파트너사 공지사항 등록
     *
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(63);

        // 파트너사 공지사항 메뉴 리스트
        PartnerNoticeDto partnerNoticeDto = new PartnerNoticeDto();
        List<PartnerNoticeDto> menuList = partnerNoticeService.getMenuList(partnerNoticeDto);

        model.addAttribute("menuList", menuList);

        hmImportFile.put("importJs", "notice/partner/regist.js.html");

        return "notice/partner/regist";
    }

    /**
     * 파트너사 공지사항 상세
     *
     * @param idx   (sns_board_notice.idx)
     * @param model
     * @return 파트너사 공지사항 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(63);
        // 파트너사 공지사항 메뉴 리스트
        PartnerNoticeDto partnerNoticeDto = new PartnerNoticeDto();
        List<PartnerNoticeDto> menuList = partnerNoticeService.getMenuList(partnerNoticeDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("idx", idx);
        hmImportFile.put("importJs", "notice/partner/view.js.html");

        return "notice/partner/view";
    }
}
