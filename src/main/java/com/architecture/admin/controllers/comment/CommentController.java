package com.architecture.admin.controllers.comment;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    /**
     * 회원 댓글 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 회원 댓글 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(23);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "comment/list.js.html");

        return "comment/list";
    }

    /**
     * 댓글 상세
     *
     * @param idx
     * @return 회원 댓글 상세
     */
    @GetMapping("/view/{idx}")
    public String detail(Model model, @PathVariable("idx") Long idx, @RequestParam(value="viewType", defaultValue = "") String viewType){
        hmDataSet.put("idx", idx);
        hmDataSet.put("viewType", viewType);
        model.addAttribute("idx", idx);
        // view pages
        hmImportFile.put("importJs", "comment/view.js.html");

        return "comment/view";
    }
}
