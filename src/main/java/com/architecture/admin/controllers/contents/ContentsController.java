package com.architecture.admin.controllers.contents;

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
@RequestMapping("/contents")
public class ContentsController extends BaseController {

    private final ContentsService contentsService;
    private final AdminService adminService;

    /**
     * 컨텐츠 등록 페이지
     *
     * @param model
     * @return
     */
    @GetMapping("/regist")
    public String regist(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(27);

        // 컨텐츠 메뉴 리스트
        ContentsDto contentsDto = new ContentsDto();
        List<ContentsDto> menuList = contentsService.getMenuList(contentsDto);

        // 관리자 소셜 계정 리스트
        List<MemberDto> adminList = adminService.getListMemberAdmin();

        model.addAttribute("menuList", menuList);
        model.addAttribute("adminList", adminList);

        hmImportFile.put("importJs", "contents/regist.js.html");

        return "contents/regist";
    }

    /**
     * 이미지 등록 레이어
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/imageLayer")
    public String imageLayer() {

        return "contents/imageLayer";
    }

    /**
     * 이미지 태그 등록 레이어
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/imageTagLayer")
    public String imageTagLayer() {

        return "contents/imageTagLayer";
    }

    /**
     * 컨텐츠 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 회원 컨텐츠 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(28);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "contents/list.js.html");

        return "contents/list";
    }

    /**
     * 컨텐츠 상세
     *
     * @param idx
     * @return 회원 컨텐츠 상세 수정 페이지
     */
    @GetMapping("/view/{idx}")
    public String detail(Model model, @PathVariable("idx") Long idx, @RequestParam(value="viewType", defaultValue = "") String viewType){
        hmDataSet.put("idx", idx);
        hmDataSet.put("viewType", viewType);
        model.addAttribute("idx", idx);
        // view pages
        hmImportFile.put("importJs", "contents/view.js.html");

        return "contents/view";
    }

    @GetMapping("/member-layer")
    public String memberLayer() {

        return "contents/memberLayer";
    }
}
