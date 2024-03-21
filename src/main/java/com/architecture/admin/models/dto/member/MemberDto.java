package com.architecture.admin.models.dto.member;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.member.title.list")
public class MemberDto implements ExcelDto {

    @ExcelColumnName(headerName = "lang.member.idx")
    private Long idx;        // 고유번호
    @ExcelColumnName(headerName = "lang.member.id")
    @Email
    private String id;          // 아이디
    private String uuid;        // 고유아이디
    private String memberUuid;        // 고유아이디
    @ExcelColumnName(headerName = "lang.member.nick")
    private String nick;        // 닉네임
    @ExcelColumnName(headerName = "lang.member.name")
    private String name;        // 이름
    private String gender;      // 성별(M: male, F: female)
    @ExcelColumnName(headerName = "lang.member.gender")
    private String genderText;  // 성별 문자변환
    @ExcelColumnName(headerName = "lang.member.simplejoin")
    private String simpleType;  // 간편가입 타입
    @ExcelColumnName(headerName = "lang.member.phone")
    private String phone;           // 전화번호
    private String partner;     // 파트너코드
    private String lang;        // 사용언어
    private String langText;    // 사용언어text
    private String loginIp;     // 로그인 아이피
    @ExcelColumnName(headerName = "lang.member.regDate")
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존
    @ExcelColumnName(headerName = "lang.member.lastLogin")
    private String lastLogin;   // 마지막 로그인
    private String lastLoginTz; // 마지막 로그인 타임존
    private String joinIp;      // 가입 아이피
    private Integer isSimple;   // 간편가입
    private Integer state;      // 상태값
    private String stateText;   // 상태값 문자변환
    private String stateBg;     // 상태 bg 색상
    private Integer isDel;      // 탈퇴 상태값
    private String isDelText;   // 탈퇴 상태값 문자변환
    private String isDelBg;     // 탈퇴 상태 bg 색상

    private Integer followCnt;  // 팔로잉 카운트
    private Integer followerCnt;// 팔로워 카운트
    private String badge;       // 뱃지여부 (Y / N)

    private Long memberIdx;         // 회원번호
    private String birth;           // 생년월일
    private String password;        // 패스워드
    private String passwordConfirm; // 패스워드 확인
    private String modiDate;        // 수정일
    private String modiDateTz;      // 수정일 타임존
    private String simpleId;        // 간편가입 아이디
    private String authToken;       // 토큰 값
    private String intro;           // 소개글
    private MemberImageDto memberImg; // 프로필 이미지
    private Integer likeCnt;        // 좋아요 수

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

    private String contents;
    private Integer marketingState;
    private String marketingStateText;
    private String marketingStateBg;
    private String marketingRegDate;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, id, nick, name, genderText, simpleType, phone, regDate, lastLogin);
    }
}
