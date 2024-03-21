package com.architecture.admin.models.dto.shopping.payments;


import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


/**
 * 결제 Dto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.payments.title")
public class PaymentsDto implements ExcelDto {
    // 결제
    @ExcelColumnName(headerName = "lang.payments.idx")
    public Integer idx;
    public String paymentType;          // 결제 유형 NORMAL, BRANDPAY, KEYIN
    @ExcelColumnName(headerName = "lang.payments.order.id")
    public String orderId;              // 주문번호
    public String paymentKey;           // 결제키
    public String amount;               // 결제금액
    public String response;             // 결제 응답값

    @ExcelColumnName(headerName = "lang.payments.mid")
    public String mId;                  // 상점아이디
    public String type;                 // 결제구분
    @ExcelColumnName(headerName = "lang.payments.method")
    public String method;               // 결제 수단
    public String secret;               // 비밀키
    @ExcelColumnName(headerName = "lang.payments.state")
    public String status;               // 결제상태
    public String cancels;              // 취소상태
    public String easyPay;              // 간편결제
    public String discount;             // 할인금액
    public String approvedAt;           // 승인시간
    public String requestedAt;          // 요청시간
    @ExcelColumnName(headerName = "lang.payments.amount")
    public Integer totalAmount;          // 총액
    public Integer balanceAmount;        // 잔액
    public String lastTransactionKey;   // 마지막 거래키
    public String isPartialCancelable;  // 부분 취소 가능 여부
    public Integer state;                // 결제 상태 0:결제대기 1:결제완료 2:결제취소 3:환불
    public String StateText;            // 결제 상태 텍스트
    public String StateBg;              // 결제 상태 생상

    @ExcelColumnName(headerName = "lang.payments.regdate")
    public String RegDate;              // 등록일시
    public String modiDate;             // 수정일시
    public String RegDateTz;            // 등록일시 타임존
    public String modiDateTz;           // 수정일시 타임존

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, orderId, mId, method, status, totalAmount, RegDate);
    }
}
