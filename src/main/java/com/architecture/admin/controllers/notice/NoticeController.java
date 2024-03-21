package com.architecture.admin.controllers.notice;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.NoticeDto;
import com.architecture.admin.services.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {
    private final NoticeService noticeService;

    /**
     * 공지사항 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 공지사항 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(14);
        
        // 공지사항 메뉴 리스트
        NoticeDto noticeDto = new NoticeDto();
        List<NoticeDto> menuList = noticeService.getMenuList(noticeDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "notice/list.js.html");

        return "notice/list";
    }

    /**
     * 공지사항 등록
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(14);

        // 공지사항 메뉴 리스트
        NoticeDto noticeDto = new NoticeDto();
        List<NoticeDto> menuList = noticeService.getMenuList(noticeDto);

        model.addAttribute("menuList", menuList);

        hmImportFile.put("importJs", "notice/regist.js.html");

        return "notice/regist";
    }

    /**
     * 공지사항 상세
     *
     * @param idx (sns_board_notice.idx)
     * @param model
     * @return 공지사항 상세 페이지
     */
    @GetMapping(value = "/view/{idx}")
    public String view(@PathVariable(name = "idx", required = false) Integer idx, Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(14);
        // 공지사항 메뉴 리스트
        NoticeDto noticeDto = new NoticeDto();
        List<NoticeDto> menuList = noticeService.getMenuList(noticeDto);

        model.addAttribute("menuList", menuList);
        model.addAttribute("idx", idx);
        hmImportFile.put("importJs", "notice/view.js.html");

        return "notice/view";
    }
    
}
