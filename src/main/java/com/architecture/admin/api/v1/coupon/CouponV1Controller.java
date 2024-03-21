package com.architecture.admin.api.v1.coupon;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.coupon.CouponDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.services.coupon.CouponService;
import com.architecture.admin.services.member.MemberService;
import com.architecture.admin.services.shopping.brand.BrandService;
import com.architecture.admin.services.shopping.category.CategoryService;
import com.architecture.admin.services.shopping.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupon")
public class CouponV1Controller extends BaseController {

    private final CouponService couponService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final MemberService memberService;

    /**
     * 쿠폰 리스트
     *
     * @param searchDto
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity contentsList(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(68);

        // list
        List<CouponDto> list = couponService.getCouponList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.coupon.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.coupon.exception.search_fail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 쿠폰 적용 대상 검색
     *
     * @param searchDto searchWord searchType rangeType
     * @return ResponseEntity
     */
    @GetMapping("/search")
    public ResponseEntity searchTarget(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(68);

        String rangeType = searchDto.getRangeType();

        JSONObject joData = new JSONObject();

        if (rangeType.equals("product")) {
            List<ProductDto> productDtoList = productService.searchProduct(searchDto);
            joData.put("list", productDtoList);
        } else if (rangeType.equals("category")) {
            List<CategoryDto> categoryDtoList = categoryService.searchCategory(searchDto);
            joData.put("list", categoryDtoList);
        } else if (rangeType.equals("brand")) {
            List<BrandDto> brandDtoList = brandService.searchBrand(searchDto);
            joData.put("list", brandDtoList);
        } else if (rangeType.equals("member")) {
            List<MemberDto> memberDtoList = memberService.searchMember(searchDto);
            joData.put("list", memberDtoList);
        }

        joData.put("params", new JSONObject(searchDto));

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.coupon.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 쿠폰 상세
     *
     * @param idx 쿠폰 idx
     * @return ResponseEntity
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx") Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(68);

        CouponDto couponDto = new CouponDto();
        couponDto.setIdx(idx);

        // 상세
        CouponDto view = couponService.getView(couponDto);

        Map<String, Object> map = new HashMap<>();
        map.put("view", view);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = super.langMessage("lang.common.success.search");

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 양식 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/sample-excel")
    public ModelAndView sampleExcelDownload(SearchDto searchDto) {

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = couponService.sampleExcelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 실패 엑셀 다운로드
     *
     * @param couponDto rangeType
     * @return
     */
    @GetMapping("/fail-excel")
    public ModelAndView failExcelDownload(CouponDto couponDto) {

        Map<String, Object> mExcelData = null;
        
        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        if (couponDto.getRangeType().equals("product")) {
            mExcelData = couponService.productFailExcelDownload(couponDto);
        }

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 엑셀 업로드
     * 
     * @param couponDto rangeType
     * @return
     */
    @PostMapping("/excel-upload")
    public ResponseEntity excelUpload(CouponDto couponDto) {
        // 관리자 접근 권한
        super.adminAccessFail(68);

        // response object
        JSONObject data = new JSONObject();

        // 업로드한 엑셀 리스트 결과 출력
        if (couponDto.getRangeType().equals("product")) {
            data = couponService.productExcelUpload(couponDto);
        }

        String sMessage = super.langMessage("lang.common.success.search");
        return displayJson(true, "1000", sMessage, data);
    }

}
