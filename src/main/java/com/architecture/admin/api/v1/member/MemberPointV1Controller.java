package com.architecture.admin.api.v1.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberPointDto;
import com.architecture.admin.services.member.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members/point")
public class MemberPointV1Controller extends BaseController {

    private final MemberPointService pointService;

    /**
     * 회원 포인트 조회
     *
     * @param memberUuid : memberUuid
     * @return : 회원 포인트
     */
    @GetMapping("")
    public ResponseEntity<String> getMemberPoint(@RequestParam String memberUuid) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        // 회원 포인트 조회
        MemberPointDto pointDto = pointService.getMemberPoint(memberUuid);

        String sMessage = super.langMessage("lang.common.success.search");

        JSONObject data = new JSONObject();
        data.put("memberPoint", new JSONObject(pointDto));

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 포인트 적립 or 사용 리스트
     *
     * @param searchDto :  type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType, page, limit
     * @return : 포인트 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<String> getPointList(SearchDto searchDto) {
        int type = searchDto.getType();

        if (type == 1) { // 적립
            super.adminAccessFail(79);
        } else if (type == 2) { // 사용
            super.adminAccessFail(80);
        }

        // 포인트 리스트
        List<MemberPointDto> pointList = pointService.getPointList(searchDto);

        JSONObject data = new JSONObject();
        data.put("list", pointList);
        data.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.contents.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }


    /**
     * 상품 주문번호 체크
     *
     * @param productOrderId : 상품 주문번호
     * @param memberUuid     : 회원 uuid
     * @return
     */
    @GetMapping("/order-id/check")
    public ResponseEntity<String> checkProductOrderId(@RequestParam String productOrderId,
                                                      @RequestParam String memberUuid) {

        MemberPointDto memberPointDto = MemberPointDto.builder()
                .productOrderId(productOrderId)
                .memberUuid(memberUuid).build();

        // 빈값이 아닌 경우만 체크
        if (!ObjectUtils.isEmpty(memberPointDto.getProductOrderId())) {
            // 상품 주문번호 체크
            pointService.checkProductOrderId(memberPointDto);
        }

        String sMessage = super.langMessage("lang.common.success.search");

        return displayJson(true, "1000", sMessage);
    }
}
