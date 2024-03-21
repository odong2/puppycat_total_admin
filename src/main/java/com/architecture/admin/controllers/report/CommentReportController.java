package com.architecture.admin.controllers.report;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.report.ReportCodeDto;
import com.architecture.admin.services.report.CommentReportService;
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
@RequestMapping("/report/comment")
public class CommentReportController extends BaseController {

    private final CommentReportService commentReportService;

    /**
     * 신고 게시물 목록
     *
     * @param model
     * @param page
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public String reportCommentLists(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(11);

        // 신고사유 list
        List<ReportCodeDto> codeList = commentReportService.getListReportCode();

        hmDataSet.put("codeList", codeList);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "report/comment/list.js.html");

        return "report/comment/list";
    }

}
