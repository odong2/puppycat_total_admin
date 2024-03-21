package com.architecture.admin.models.dto.shopping.deliveryPolicy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryPolicyDto {
    /**
     * shop_delivery_policy
     */
    private Integer idx;
    private Integer partnerIdx;                 // shop_partner.idx
    private Integer mainIdx;                    // shop_partner.main_idx
    private String partnerId;                   // shop_partner.id
    private String name;                        // 배송정책명
    private Integer property;                   // 배송속성 1:일반 2:새벽 3:특급
    private String propertyText;                // 배송속성 문자변환
    private Integer companyIdx;                 // 택배사
    private Integer returnCompanyIdx;           // 교환/반품 택배사
    private String releaseAddress;              // 출고지
    private String releaseAddressDetail;        // 출고지 상세주소
    private String returnAddress;               // 교환/반품 회수지
    private String returnAddressDetail;         // 교환/반품 회수지 상세주소
    private Integer deadline;                   // 출고기간 당일~15일
    private String sameDayDeliveryTime;         // 당일배송 시간 설정
    private String sameDayDeliveryTimeHour;     // 당일배송 시간 설정(시)
    private String sameDayDeliveryTimeMinute;   // 당일배송 시간 설정(분)
    private Integer isCombine;                  // 묶음배송 여부
    private Integer isIsland;                   // 도서산간 배송여부
    private Integer paymentType;                // 배송비 유형
    private Integer freeCondition;              // 무료 배송 조건 금액
    private Integer basicFee;                   // 기본배송비
    private Integer islandFee;                  // 도서산간 배송비
    private Integer exchangeFee;                // 교환 배송비
    private Integer returnFee;                  // 반품 배송비
    private Integer state;                      // 상태값
    private String stateText;                   // 상태값 문자변환
    private String stateBg;                     // 상태값 뱃지
    private String modiDate;                    // 수정일
    private String modiDateTz;                  // 수정일 타임존
    private String regDate;                     // 등록일
    private String regDateTz;                   // 등록일 타임존
    private String isCombineText;       // 묶음배송 여부 텍스트
    private String isIslandText;        // 도서산간 배송여부 텍스트
    private String paymentTypeText;     // 배송비 유형 텍스트

    /**
     * shop_delivery_company
     */
    private String companyName; // 택배사명
    private String companyCode; // 택배사 코드
    private String returnCompanyName; // 택배사명
    private String returnCompanyCode; // 택배사 코드

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

    // etc
    private Integer canDel;     // 삭제 가능 여부

}
