package com.architecture.admin.models.dto.member;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.point.title.list")
public class MemberPointDto implements ExcelDto {
    @ExcelColumnName(headerName = "lang.point.idx")
    private Long idx;
    @ExcelColumnName(headerName = "lang.member.id")
    private String memberId;        // 회원 id
    @ExcelColumnName(headerName = "lang.member.nick")
    private String memberNick;      // 회원 닉네임
    @ExcelColumnName(headerName = "lang.point.title")
    private String title;           // 지급/사용 내역
    @ExcelColumnName(headerName = "lang.point.pay.subtraction.position")
    private String position;        // 지급/사용 위치(상품구매, 이벤트, 후기작성)
    @ExcelColumnName(headerName = "lang.point.pay.point")
    private String pointText;            // 포인트 문자열
    @ExcelColumnName(headerName = "lang.point.rest.point")
    private String restPointText;
    @ExcelColumnName(headerName = "lang.point.admin.id")
    private String admin;           // 지급/차감 관리자 아이디
    @ExcelColumnName(headerName = "lang.point.partner.id")
    private String partner;         // 지급/차감 파트너 아이디
    @ExcelColumnName(headerName = "lang.point.product.order.id")
    private String productOrderId;  // 상품주문번호
    @ExcelColumnName(headerName = "lang.point.expireDate")
    private String expireDate;
    @ExcelColumnName(headerName = "lang.point.regdate")
    private String regdate;
    private String memberUuid;
    private Integer point;          // 보유 포인트
    private Integer restPoint;      // 잔여 포인트
    private Integer savePoint;      // 적립 포인트
    private Integer usePoint;       // 사용 포인트
    private Integer expirePoint;    // 만료 포인트
    private Integer state;
    private String regDateTz;
    private String expireDateTz;

    /**
     * point_log
     */
    private Long pointSaveIdx;      // 적립 포인트 idx
    private Integer type;           // 1: 적립, 2:사용, 3:소멸

    /**
     * member
     */
    private Long memberIdx;         // 회원 idx

    // 기타
    private Integer subResultPoint;      // 차감 결과 포인트
    private String typeText;             // 지급/사용/소멸 문자열 타입
    private String totalSavePointText;   // 총 포인트
    private String totalUsePointText;    // 총 사용 포인트
    private String totalExpirePointText; // 총 만료 포인트

    private String usePointText;
    private String savePointText;
    private String expirePointText;

    private List<MemberPointDto> startDateList;
    private List<MemberPointDto> middleDateList;
    private List<MemberPointDto> endDateList;

    // sql
    private Long insertedIdx;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, memberId, memberNick, title, position, pointText, restPointText, admin, partner, productOrderId, expireDate, regdate);
    }
}
