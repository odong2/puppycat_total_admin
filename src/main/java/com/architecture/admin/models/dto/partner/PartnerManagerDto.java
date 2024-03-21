package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerManagerDto {

    private Integer idx;        // idx
    private Integer partnerIdx; // 파트너 idx
    private Integer type;       // 1:상품, 2:CS, 3:배송, 4:정산, 5:운영
    private String managerName; // 매니저 이름
    private String phone;       // 휴대폰
    private String tel;         // 연락처
    private String email;       // 이메일
    private Integer state;      // 상태값
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존

    // 기타
    private String phone1;
    private String phone2;
    private String phone3;
    private String tel1;
    private String tel2;
    private String tel3;
}
