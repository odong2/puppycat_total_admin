package com.architecture.admin.services.coupon;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.excel.ExcelLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.coupon.CouponDaoSub;
import com.architecture.admin.models.daosub.product.ProductDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.coupon.CouponDto;
import com.architecture.admin.models.dto.coupon.excel.CouponMemberExcelDto;
import com.architecture.admin.models.dto.coupon.excel.CouponProductExcelDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CouponService extends BaseService {

    private final CouponDaoSub couponDaoSub;
    private final ProductDaoSub productDaoSub;
    @Value("${coupon.range.product}")
    private Integer product;
    @Value("${coupon.range.category}")
    private Integer category;
    @Value("${coupon.range.brand}")
    private Integer brand;

    private final ExcelData excelData;
    private final ExcelLibrary excelLibrary;

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 쿠폰 리스트
     *
     * @param searchDto
     * @return list
     */
    public List<CouponDto> getCouponList(SearchDto searchDto) {
        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = couponDaoSub.getCouponTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 콘텐츠 리스트
        List<CouponDto> list = couponDaoSub.getCouponList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 쿠폰 상세
     *
     * @param couponDto 쿠폰 idx
     * @return CouponDto
     */
    public CouponDto getView(CouponDto couponDto) {
        Integer idx = couponDto.getIdx();

        if (ObjectUtils.isEmpty(idx)) {
            throw new CustomException(CustomError.COUPON_IDX_ERROR);
        }

        // 쿠폰 상세
        CouponDto view = couponDaoSub.getCouponView(couponDto);

        if (ObjectUtils.isEmpty(view)) {
            throw new CustomException(CustomError.COUPON_IDX_ERROR);
        }

        // 타겟 쿠폰 정보 조회 & 세팅
        if (Objects.equals(view.getTypeIdx(), 2)) {
            List<MemberDto> memberList = couponDaoSub.getCouponMemberMapping(couponDto);
            view.setMemberList(memberList);
        }

        // 적용 대상 매핑 정보 조회 & 세팅
        List<CouponDto> targetList = new ArrayList<>();
        if (Objects.equals(view.getRangeIdx(), product)) {
            targetList = couponDaoSub.getCouponProductMapping(couponDto);
        } else if (Objects.equals(view.getRangeIdx(), category)) {
            targetList = couponDaoSub.getCouponCategoryMapping(couponDto);
        } else if (Objects.equals(view.getRangeIdx(), brand)) {
            targetList = couponDaoSub.getCouponBrandMapping(couponDto);
        }
        view.setTargetList(targetList);

        // 문자변환
        stateText(view);

        return view;
    }

    /**
     * 양식 엑셀 다운로드
     *
     * @param searchDto rangeType
     * @return
     */
    public Map<String, Object> sampleExcelDownload(SearchDto searchDto) {

        Map<String, Object> map = new HashMap<>();

        // 엑셀 데이터 생성
        if (searchDto.getRangeType().equals("member")) {
            map = excelData.createExcelData(new ArrayList<>(), CouponMemberExcelDto.class);
        } else if (searchDto.getRangeType().equals("product")) {
            map = excelData.createExcelData(new ArrayList<>(), CouponProductExcelDto.class);
        }

        return map;
    }

    /**
     * 상품 엑셀 업로드
     *
     * @param couponDto excelFile
     * @return
     */
    public JSONObject productExcelUpload(CouponDto couponDto) {

        // 엑셀 파일
        MultipartFile excelFile = couponDto.getExcelFile();

        // 엑셀 데이터 자바 객체로 변환
        List<CouponProductExcelDto> excelProductList = excelLibrary.parseExcelToObject(excelFile, CouponProductExcelDto.class);

        // 총 row 수
        int fail = 0;
        int success = 0;

        // 조회용 idxList 세팅
        List<Integer> idxList = new ArrayList<>();
        excelProductList.forEach(dto -> idxList.add(dto.getIdx()));

        // 실패된 idx 담을 List 세팅
        List<Integer> failIdxList = new ArrayList<>();

        // 상품 정보 조회
        List<ProductDto> productList = productDaoSub.getProductListByIdx(idxList);

        for (CouponProductExcelDto excelProductDto : excelProductList) {

            // 조회된 정보 세팅
            for (int j = 0; j < productList.size(); j++) {
                ProductDto productDto = productList.get(j);

                if (Objects.equals(excelProductDto.getIdx(), productDto.getIdx())) {
                    excelProductDto.setProductName(productDto.getProductName());
                    excelProductDto.setResult(1);
                    excelProductDto.setResultText(super.langMessage("lang.coupon.excel.success"));
                    excelProductDto.setResultBg("badge-success");
                    productList.remove(j);
                    success++;
                    break;
                }
            }

            // 실패된 정보 세팅
            if (ObjectUtils.isEmpty(excelProductDto.getProductName())) {
                excelProductDto.setProductName(" ");
                excelProductDto.setResult(0);
                excelProductDto.setResultText(super.langMessage("lang.coupon.excel.fail"));
                excelProductDto.setResultBg("badge-danger");
                failIdxList.add(excelProductDto.getIdx());
                fail++;
            }

        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", excelProductList);
        map.put("successCount", success);
        map.put("failCount", fail);
        map.put("failIdxList", failIdxList);

        return new JSONObject(map);
    }

    /**
     * 상품 실패 엑셀 다운로드
     *
     * @param couponDto rangeType failIdxList
     * @return
     */
    public Map<String, Object> productFailExcelDownload(CouponDto couponDto) {

        Map<String, Object> map;
        List<CouponProductExcelDto> list = new ArrayList<>();

        // 엑셀 데이터 생성
        List<Integer> failIdxList = couponDto.getFailIdxList();
        for (Integer idx : failIdxList) {
            CouponProductExcelDto dto = CouponProductExcelDto.builder()
                    .idx(idx)
                    .resultText(super.langMessage("lang.coupon.excel.fail"))
                    .build();
            list.add(dto);
        }
        map = excelData.createExcelData(list, CouponProductExcelDto.class);

        List<String> headList = new ArrayList<>();
        headList.add(super.langMessage("lang.coupon.num"));
        headList.add(super.langMessage("lang.coupon.excel.result"));
        map.put("filename", "product_upload_fail");
        map.put("head", headList);

        return map;
    }

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 쿠폰 발급 방식 리스트
     *
     * @return 카운트 값
     */
    public List<CouponDto> getCouponTypeList() {
        return couponDaoSub.getCouponTypeList();
    }

    /**
     * 쿠폰 사용 범위 리스트
     *
     * @return 카운트 값
     */
    public List<CouponDto> getCouponRangeList() {
        return couponDaoSub.getCouponRangeList();
    }

    /*****************************************************
     *  SubFunction - Insert
     ****************************************************/

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

    /*****************************************************
     *  SubFunction - ETC
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<CouponDto> list) {
        for (CouponDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(CouponDto dto) {
        // 쿠폰 상태
        if (!ObjectUtils.isEmpty(dto.getState())) {
            if (dto.getState() == 0) { // 발급중단
                dto.setStateText(super.langMessage("lang.coupon.state.stop"));
                dto.setStateBg("badge-danger");
            } else if (dto.getState() == 1) { // 발급완료
                dto.setStateText(super.langMessage("lang.coupon.state.complete"));
                dto.setStateBg("badge-success");
            } else if (dto.getState() == 2) { // 발급대기
                dto.setStateText(super.langMessage("lang.coupon.state.wait"));
                dto.setStateBg("badge-warning");
            } else if (dto.getState() == 3) { // 발급회수
                dto.setStateText(super.langMessage("lang.coupon.state.collect"));
                dto.setStateBg("badge-info");
            }
        }

        // 발급 제한
        if (!ObjectUtils.isEmpty(dto.getIsLimit())) {
            if (dto.getIsLimit() == 0) { // 제한 없음
                dto.setIsLimitText(super.langMessage("lang.coupon.no.limit"));
            } else if (dto.getIsLimit() == 1) { // 제한 있음
                dto.setIsLimitText(super.langMessage("lang.coupon.limit"));
            }
        }

        // 발급 타입
        if (!ObjectUtils.isEmpty(dto.getIssueCondition())) {
            if (dto.getIssueCondition() == 1) {  // 발급일
                dto.setIssueConditionText(super.langMessage("lang.coupon.issue.type.issue.date"));
            } else if (dto.getIssueCondition() == 2) { // 사용 기한
                dto.setIssueConditionText(super.langMessage("lang.coupon.issue.type.use.date"));
            }
        }

    }

}
