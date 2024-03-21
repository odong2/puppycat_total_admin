package com.architecture.admin.controllers.coupon;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.coupon.CouponDto;
import com.architecture.admin.services.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController {

    private final CouponService couponService;

    /**
     * 쿠폰 리스트
     *
     * @param model
     * @param page
     * @param searchDto
     * @return 회원 댓글 페이지
     */
    @GetMapping("")
    public String lists(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(68);

        // 쿠폰 발급 방식 정보 가져오기
        List<CouponDto> couponTypeList = couponService.getCouponTypeList();
        // 쿠폰 사용 범위 정보 가져오기
        List<CouponDto> couponRangeList = couponService.getCouponRangeList();

        // set view data
        model.addAttribute("couponTypeList", couponTypeList);
        model.addAttribute("couponRangeList", couponRangeList);
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        hmImportFile.put("importJs", "coupon/list.js.html");

        return "coupon/list";
    }

    /**
     * 쿠폰 상세
     *
     * @param idx
     * @return 쿠폰 상세
     */
    @GetMapping("/view/{idx}")
    public String detail(Model model, @PathVariable("idx") Long idx){
        hmDataSet.put("idx", idx);

        // 쿠폰 발급 방식 정보 가져오기
        List<CouponDto> couponTypeList = couponService.getCouponTypeList();
        // 쿠폰 사용 범위 정보 가져오기
        List<CouponDto> couponRangeList = couponService.getCouponRangeList();

        // set view data
        model.addAttribute("couponTypeList", couponTypeList);
        model.addAttribute("couponRangeList", couponRangeList);
        // view pages
        hmImportFile.put("importJs", "coupon/view.js.html");
        return "coupon/view";
    }

    /**
     * 쿠폰 추가
     *
     * @return 쿠폰 추가
     */
    @GetMapping("/regist")
    public String register(Model model) {
        // 관리자 접근 권한
        super.adminAccessFail(68);

        // 쿠폰 발급 방식 정보 가져오기
        List<CouponDto> couponTypeList = couponService.getCouponTypeList();
        // 쿠폰 사용 범위 정보 가져오기
        List<CouponDto> couponRangeList = couponService.getCouponRangeList();

        // set view data
        model.addAttribute("couponTypeList", couponTypeList);
        model.addAttribute("couponRangeList", couponRangeList);

        // view pages
        hmImportFile.put("importJs", "coupon/regist.js.html");
        return "coupon/regist";
    }

    /**
     * 쿠폰 적용 대상 검색 레이어 호출
     *
     * @return 쿠폰 추가
     */
    @GetMapping("/search")
    public String targetSearchLayer() {
        return "coupon/searchLayer";
    }

    /**
     * 엑셀 일괄 업로드 레이어 호출
     *
     * @return 쿠폰 추가
     */
    @GetMapping("/excel")
    public String excelUploadLayer() {
        return "coupon/excelLayer";
    }

}
