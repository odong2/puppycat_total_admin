package com.architecture.admin.models.dto.partner;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
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
@ExcelFileName(filename = "lang.partner.title.list")
public class PartnerDto implements ExcelDto {

    @ExcelColumnName(headerName = "lang.partner.idx")
    private Integer idx;            // 고유번호
    @ExcelColumnName(headerName = "lang.partner.id")
    private String id;              // 파트너 id
    @ExcelColumnName(headerName = "lang.partner.email")
    private String email;           // 파트너 이메일
    private String password;        // 패스워드
    private String passwordConfirm; // 패스워드 확인
    private Integer level;          // 관리자 레벨
    private String loginIp;         // 로그인ip
    private String joinIp;          // 가입ip
    private Integer state;          // 상태
    private String stateText;       // 상태 문자 변환
    private String stateBg;         // 상태 bg 색상
    private String lastLoginDateTz; // 마지막 로그인 타임존
    private String regDateTz;       // 등록일 타임존
    private String modiDate;        // 수정일
    private String modiDateTz;      // 수정일 타임존
    private String partnerCode;     // 파트너 고유 코드
    private Integer mainIdx;        // 대표 파트너 idx

    /**
     * shop_partner_detail
     */
    @ExcelColumnName(headerName = "lang.partner.president.name")
    private String president;           // 대표자 명
    private String presidentTel;        // 대표자 연락처
    private String businessNumber;      // 사업자 번호
    private String companyNumber;       // 법인 등록번호
    @ExcelColumnName(headerName = "lang.partner.company.name")
    private String companyName;         // 상호명
    private String taxType;             // 과세여부
    private String presidentType;       // 대표구성
    @ExcelColumnName(headerName = "lang.partner.businessType")
    private String businessType;        // 업태
    @ExcelColumnName(headerName = "lang.partner.businessItem")
    private String businessItem;        // 업종
    private String approvalNumber;      // 통신판매업 신고번호
    private String companyType;         // 사업자유형
    private String companyZipcode;      // 사업장 우편번호
    private String companyAddr;         // 사업장 주소
    private String companyAddrDetail;   // 사업장 상세주소
    @ExcelColumnName(headerName = "lang.partner.companyTel")
    private String companyTel;          // 유선전화(대표번호)
    private String website;             // 파트너 홈페이지 주소

    /**
     * partner_info
     */
    private String txseq;               // 본인인증거래번호
    private String name;                // 본인인증 이름
    @ExcelColumnName(headerName = "lang.partner.phone")
    private String phone;               // 전화번호
    private String ci;                  // 개인 식별 고유값
    private String di;                  // di
    private String ip;                  // 인증 아이피

    /**
     * partner_bank
     */
    private Integer bankIdx;            // 은행 idx
    private String accountHolder;       // 예금주
    private String accountNumber;       // 계좌 번호
    private Integer partnerBankState;   // 0: 거절, 1: 승인, 2: 대기

    /**
     * partner_manager
     */
    // cs 매니저
    private String csManagerName;   // cs 매니저 이름
    private String csManagerEmail;  // cs 매니저 이메일
    private String csManagerTel;    // cs 매니저 연락처
    private String csManagerPhone;  // cs 매니저 휴대폰
    
    // 배송 매니저
    private String deliverManagerName;  // 배송 매니저 이름
    private String deliverManagerEmail; // 배송 매니저 이메일
    private String deliverManagerTel;   // 배송 매니저 연락처
    private String deliverManagerPhone; // 배송 매니저 휴대폰

    // 정산 매니저
    private String calculatorManagerName;  // 정산 매니저 이름
    private String calculatorManagerEmail; // 정산 매니저 이메일
    private String calculatorManagerTel;   // 정산 매니저 연락처
    private String calculatorManagerPhone; // 정산 매니저 휴대폰

    // 정산 매니저
    private String administrationManagerName;  // 정산 매니저 이름
    private String administrationManagerEmail; // 정산 매니저 이메일
    private String administrationManagerTel;   // 정산 매니저 연락처
    private String administrationManagerPhone; // 정산 매니저 휴대폰

    // 상품 매니저
    private String productManagerName;    // 상품 매니저 이름
    private String productManagerEmail;   // 상품 매니저 이메일
    private String productManagerTel;     // 상품 매니저 연락처
    private String productManagerPhone;   // 상품 매니저 휴대폰

    /**
     * 파일 관련
     */
    private List<MultipartFile> businessFile; // 사업자 등록증
    private List<MultipartFile> approvalFile; // 통신판매업 사본
    private List<MultipartFile> accountFile;  // 통장 사본
    private List<MultipartFile> uploadFile;   // 기타 서류
    private List<PartnerImgDto> imgList;      // 서류 이미지 리스트

    /**
     * shop_partner_sub
     */
    private String subName;         // 이름
    private String grade;           // 직급
    private String part;            // 부서
    private String tel;             // 전화번호

    /**
     * shop_partner_restrain
     */
    private String startDate;   // 제재 시작일
    private String endDate;     // 제재 종료일

    // sql
    private Integer insertedId;
    private Integer lastDateRow;

    private String code;        // 코드값
    private Boolean result;

    @ExcelColumnName(headerName = "lang.partner.regdate")
    private String regDate;         // 등록일(UTC)
    @ExcelColumnName(headerName = "lang.partner.last.login.date")
    private String lastLoginDate;   // 마지막 로그인(UTC)

    // 기타 : 비밀번호 변경 폼
    private Long nameIdx;       // 권한 이름 idx
    private Long oldNameIdx;    // 이전 권한 이름 idx
    private String grantName;   // 권한 이름
    private String oldPassword; // 이전 비밀번호
    private String newPassword; // 변경할 비밀번호
    private Boolean rememberId; // 아이디 기억하기
    private String phone1;
    private String phone2;
    private String phone3;
    private String tel1;
    private String tel2;
    private String tel3;
    private String reason; // 반려 사유
    private String presidentTel1;
    private String presidentTel2;
    private String presidentTel3;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, id, email, president, companyName, businessType, businessItem, companyTel, phone, regDate, lastLoginDate);
    }
}
