package com.architecture.admin.controllers.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.services.member.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member/point")
public class MemberPointController extends BaseController {

    private final MemberPointService pointService;

    /**
     * 회원 포인트 적립 목록
     *
     * @param model     : page, searchDto
     * @param page      : page
     * @param searchDto : searchDto
     * @return 포인트 적립 목록 페이지
     */
    @GetMapping("/save/list")
    public String saveList(Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(79);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "member/point/saveList.js.html");

        return "member/point/saveList";
    }

    /**
     * 회원 포인트 사용 목록
     *
     * @param model     : page, searchDto
     * @param page      : page
     * @param searchDto : searchDto
     * @return 포인트 사용 목록 페이지
     */
    @GetMapping("/use/list")
    public String useList(Model model,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(80);

        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "member/point/useList.js.html");

        return "member/point/useList";
    }

    /**
     * 포인트 리스트 엑셀 다운로드
     *
     * @param searchDto :  type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(SearchDto searchDto) {

        // 포인트 리스트(적립 or 사용)
        Map<String, Object> excelData = pointService.excelDownload(searchDto);

        // 엑셀 데이터 생성
        return new ModelAndView(new ExcelXlsxView(), excelData);
    }

}
