package com.architecture.admin.models.dto.member;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.member.out.title.list")
public class OutMemberDto implements ExcelDto {
    /**
     * sns_member_out
     */
    @ExcelColumnName(headerName = "lang.member.num")
    private Long idx;   // 일련번호
    private Integer codeIdx;    // 탈퇴사유 sns_member_out_code.idx
    private Long memberIdx;       // sns_member.idx
    @ExcelColumnName(headerName = "lang.member.id")
    private String id;      // 회원 아이디
    private String uuid;    // 고유아이디
    private String password;      // 비밀번호
    @ExcelColumnName(headerName = "lang.member.nick")
    private String nick;    // 회원 닉네임
    private String partner;     // 파트너명
    private String lang;        // 사용언어
    private String langText; // 사용언어 문자변환
    private Integer isSimple;   // 간편가입
    @ExcelColumnName(headerName = "lang.member.simplejoin")
    private String simpleType;  // 간편가입 타입
    private String loginIp;     // 로그인 아이피
    private String joinIp;      // 가입 아이피
    private String lastLogin;   // 마지막 로그인(UTC)
    private String lastLoginTz; // 마지막 로그인 타임존
    private String regDate;     // 가입일
    private String regDateTz;   // 가입일 타임존
    private Integer state;       // 탈퇴상태 [탈퇴확정:1, 탈퇴대기:2, 복구:3]
    @ExcelColumnName(headerName = "lang.member.state")
    private String stateText;   // 탈퇴상태 문자변환
    private String stateBg;    // 탈퇴상태 bg 색상
    @ExcelColumnName(headerName = "lang.member.outRegDate")
    private String outRegDate;      // 탈퇴신청일
    private String outRegDateTz;    // 탈퇴신청일 타임존
    @ExcelColumnName(headerName = "lang.member.outDate")
    private String outDate;         // 탈퇴확정일
    private String outDateTz;       // 탈퇴확정일 타임존

    /**
     * sns_member_out_code
     */
    @ExcelColumnName(headerName = "lang.member.out.reason")
    private String codeName; // 탈퇴사유

    /**
     * sns_member_out_reason
     */
    @ExcelColumnName(headerName = "lang.member.out.reason")
    private String reason; // 탈퇴 상세사유

    /**
     * sns_member_info
     **/
    private String name;        // 이름
    private String phone;       // 전화번호
    private String birth;       // 생년월일
    private String gender;      // 성별(M: male, F: female)
    private String genderText; // 성별 문자변환

    /**
     * sns_member_simple
     **/
    private String simpleId;    // 간편가입 넘어오는 아이디
    private String authToken;   // 토큰 값 (refresh)

    // sql
    private Long insertedIdx;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, id, nick, simpleType, stateText, outRegDate, outDate, codeName, reason);
    }

}
