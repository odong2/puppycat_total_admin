package com.architecture.admin.models.dto.shopping.product;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.models.dto.pet.HealthDto;
import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import com.architecture.admin.models.dto.shopping.certification.CertificationDto;
import com.architecture.admin.models.dto.shopping.deliveryPolicy.DeliveryPolicyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.product.partner.title.management")
public class ProductDto implements ExcelDto {

    /**
     * shop_product_mapping
     */
    @ExcelColumnName(headerName = "lang.product.partner.idx")
    private Integer idx;                // idx
    private Long partnerProductIdx;     // 파트너 상품 idx
    private String brandName;           // 브랜드명
    private Integer brandGroupIdx;      // 브랜드 그룹 idx

    private Integer volume;                         // 총 용량
    private Integer volumeUnit;                     // 총 용량 표시 단위 shop_ingredient_unit.idx
    private Integer standard;                       // 가격 표기 기준 용량
    private Integer standardUnit;                   // 가격 표기 기준 단위 s
    /**
     * shop_product
     */
    private Integer brandIdx;           // 브랜드 idx
    @ExcelColumnName(headerName = "lang.product.partner.name")
    private String productName;         // 상품명
    private Integer certificationType;  // 인증유형 0:인증대상 아님, 1:인증대상, 2:상품상세설명내 표기
    private Integer state;              // 상태값
    @ExcelColumnName(headerName = "lang.product.partner.state")
    private String stateText;           // 상태값 문자변환
    private String stateBg;

    /**
     * shop_partner_product
     */
    private Integer partnerIdx;              // 파트너 idx
    private Integer partnerMainIdx;          // main 파트너 idx
    private Integer productState;            // 상태값
    private Integer open;                    // 공개여부
    private Integer openState;               // 공개여부
    @ExcelColumnName(headerName = "lang.product.partner.open.state")
    private String openStateText;            // 공개여부 문자변환
    private String openStateBg;              // 공개여부 bg 색상
    private String productStateText;         // 상태값 문자변환
    private String productStateBg;           // 상태 bg 색상
    private String partnerProductName;       // 상품명
    private String modiDate;                // 수정일
    private String modiDateTz;               // 수정일 타임존
    private Integer minPriceState;      // 최저가 여부 shop_product_mapping.state
    @ExcelColumnName(headerName = "lang.product.partner.show.state")
    private String minPriceStateText;   // 최저가 여부 문자변환
    private String minPriceStateBg;     // 최저가 여부 bg 색상

    /**
     * product_benefit
     */
    private Integer buy;        // 구매확정 포인트
    private Integer review;     // 일반리뷰 작성 포인트
    private Integer review30;   // 한달사용 리뷰 작성 포인트
    private Integer isAuto;     // 자동구매확정 포인트 지급여부
    private Integer benefitState;     // 포인트 지급 여부 state
    @ExcelColumnName(headerName = "lang.product.partner.benefit.setting")
    private String benefitStateText;  // 포인트 지급 여부 문자변환
    private String benefitStateBg;    // 포인트 지급 여부 bg 색상

    /**
     * product_price
     */
    private Integer price;              // 정상가
    private Integer orginPrice;         // 정상가
    private Integer minPrice;           // 최저가
    private Integer maxPrice;           // 최저가
    private Integer eventPrice;         // 이벤트가
    private Integer nowMinPrice;        // 현재 노출 최저가
    @ExcelColumnName(headerName = "lang.product.partner.lowest.price.linked")
    private Integer sync;               // 최저가 연동 여부
    private String syncText;            // 최저가 연동 문자변환
    private String syncBg;              // 최저가 연동 bg 색상


    /**
     * product_stock
     */
    @ExcelColumnName(headerName = "lang.product.partner.stock.count")
    private Integer stock;          // 남은재고
    @ExcelColumnName(headerName = "lang.product.partner.placeholder.purchase.restrictions")
    private Integer unlimit;        // 1: 구매제한 없음
    private Integer alarm;          // 품절 알림 수신 여부
    @ExcelColumnName(headerName = "lang.product.partner.stock.alarm")
    private String alarmText;       // 품절 알림 여부 문자변환
    private String alarmBg;         // 품절 알림 여부 bg 색상
    @ExcelColumnName(headerName = "lang.product.partner.stock.alarm.cnt")
    private Integer alarmCount;     // 알림발송 카운트

    /**
     * product_detail
     */
    private String detail;     // 상품상세


    /**
     * shop_delivery_policy
     */
    private Integer deliveryPolicyIdx;        // 배송정책idx
    private Integer paymentType;              // 배송비 유형 1:무료 2:유료 3:조건부무료
    @ExcelColumnName(headerName = "lang.product.partner.delivery.fee.type")
    private String paymentTypeText;           // 배송비 유형 문자변환
    private String paymentTypeBg;             // 배송비 유형 bg 색상
    private Integer isCombine;                // 묶음배송 여부
    @ExcelColumnName(headerName = "lang.product.partner.combined.shipping")
    private String isCombineText;             // 묶음배송 문자변환
    private String isCombineBg;               // 묶음배송 bg 색상


    /**
     * product_price
     */
    @ExcelColumnName(headerName = "lang.product.partner.price")
    private Integer salePrice;          // 판매가
    @ExcelColumnName(headerName = "lang.product.partner.set.min.price")
    private Integer discountPrice;      // 할인가
    @ExcelColumnName(headerName = "lang.product.partner")
    private String companyName;         // 파트너 상호명
    @ExcelColumnName(headerName = "lang.product.partner.code")
    private String partnerCode;         // 파트너 코드
    @ExcelColumnName(headerName = "lang.product.partner.regdate")
    private String regDate;            // 등록일
    private String regDateTz;          // 등록일 타임존
    @ExcelColumnName(headerName = "lang.product.partner.approvaldate")
    private String approvalDate;        // 승인일
    private String approvalDateTz;     // 승인일 타임존

    /**
     * shop_product_attribute_set
     */
    private String attributeSetIdx;     // shop_product_attribute_set.idx
    private String attributeSetName;    // 상품 정보 고시 유형
    private String attributeName;       // 상품 정보 고시 표기내용
    private String attributeValue;      // 상품 정보 고시 표기내용 정보

    /**
     * shop_product_certification
     */
    private Integer certificationIdx;   // shop_product_certification.idx
    private String certificationName;   // 인증유형
    private Integer certificationNum;   // 인증번호

    /**
     * shop_partner_product_return_reason
     */
    private String returnReason;        // 반려사유
    private String returnReasonAdmin;   // 반려사유 등록 어드민

    private List<CertificationDto> certificationList;   // 인증유형
    private List<CategoryDto> categoryList;             // 카테고리 리스트
    private List<AttributeSetDto> attributeList;        // 상품정보 제공고시
    private List<AllergyDto> ingredientList;         // 상품 성분
    private List<MultipartFile> fileData;               // 파일 목록(이미지 파일)
    private List<ProductImgDto> thumbnailList;          // 썸네일
    private DeliveryPolicyDto deliveryList;             // 배송정보
    private Integer partnerAttributeUseState;           // MP상품의 제공고시가 없어서 파트너 상품 사용하면 :1
    private List<HealthDto> healthList;             // 건강질환

    // sql
    private Long insertedIdx;       // insert idx
    private Integer affectedRow;    // 처리 row수

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, productName, stateText, openStateText, minPriceStateText, benefitStateText, sync, stock, unlimit, alarmText, alarmCount, paymentTypeText, isCombineText, salePrice, discountPrice, companyName, partnerCode, regDate, approvalDate);
    }
}
