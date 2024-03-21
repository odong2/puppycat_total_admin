package com.architecture.admin.controllers.follow;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.services.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/follow")
public class FollowController extends BaseController {

    private final FollowService followService;

    /**
     * 회원 팔로우 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 팔로우 리스트 페이지
     */
    @GetMapping("/list")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(8);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "follow/list.js.html");

        return "follow/list";
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView followListExcel(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(8);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = followService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }
}
