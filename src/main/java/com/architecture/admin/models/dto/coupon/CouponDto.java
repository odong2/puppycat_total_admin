package com.architecture.admin.models.dto.coupon;

import com.architecture.admin.models.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {

    /**
     * shop_coupon
     */
    private Integer idx;        // 일련번호
    private String name;        // 쿠폰명
    private String managementName; // 쿠폰관리명
    private Integer adminIdx;   // total_admin.idx
    private String adminId;     // total_admin.id
    private String host;        // 부담 주체
    private Integer typeIdx;    // shop_coupon_type.idx 발급방식
    private String typeName;    // 쿠폰 발급 방식 이름
    private Integer rangeIdx;   // shop_coupon_range.idx 사용범위
    private String rangeName;   // 쿠폰 사용 범위 이름
    private Integer state;      // 상태값 0:발급중단 1:발급완료 2:발급대기 3:발급회수
    private String stateText;   // 상태값 문자변환
    private String stateBg;     // 상태값 뱃지
    private Integer qty;        // 인당 발급 제한 수량
    private Integer isLimit;    // 제한 여부 0:제한X 1:제한O
    private String isLimitText; // 제한 여부 문자변환
    private String issueStart;  // 발급시작일
    private String issueEnd;    // 발급종료일
    private String useStart;    // 쿠폰사용 시작일
    private String useEnd;      // 쿠폰사용 만료일
    private Integer useDays;    // 사용 가능 일 수
    private Integer discount;   // 할인금액
    private Integer useCondition;     // 사용 조건 금액
    private Integer issueCondition;   // 발급 조건 1:발급일 2:사용기한
    private String issueConditionText;  // 발급 조건 문자변환
    private String modiDate;    // 수정일
    private String modiDateTz;  // 수정일 타임존
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존

    /**
     * shop_coupon_member
     */
    private String expireDate;      // 만료일
    private String expireDateTz;    // 만료일 타임존

    // etc
    private Integer couponIdx;      // 쿠폰 idx
    private Integer targetIdx;      // 타겟 idx
    private List<String> memberUuidList; // 타겟쿠폰 적용 회원 uuid 리스트
    private List<Integer> productIdxList;     // 상품 idx 리스트
    private List<Integer> brandIdxList;       // 브랜드 idx 리스트
    private List<Integer> categoryIdxList;    // 카테고리 idx 리스트
    private List<CouponDto> targetList;       // 적용 대상 리스트
    private List<MemberDto> memberList;       // 타겟 쿠폰 회원 리스트
    private MultipartFile excelFile;          // 대량 업로드 엑셀 파일
    private String rangeType;                 // range 문자값
    private List<Integer> failIdxList;        // 엑셀 실패 idx 리스트

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;
}
