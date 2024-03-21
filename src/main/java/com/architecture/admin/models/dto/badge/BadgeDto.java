package com.architecture.admin.models.dto.badge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BadgeDto {

    private Integer idx;            // 고유번호
    private String memberUuid;      // 회원번호
    private Long memberIdx;         // 회원번호
    @Email
    private String memberId;        // 회원 아이디
    private String memberNick;      // 회원 닉네임
    private Integer memberState;    // 회원 탈퇴
    private Integer followerCnt;    // 팔로워수
    private String memberStateText; // 회원 탈퇴 상태값
    private String memberStateBg;   // 회원 탈퇴 배경값
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

}
