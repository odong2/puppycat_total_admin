package com.architecture.admin.models.dto.follow;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
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
@ExcelFileName(filename = "lang.follow.title.list")
public class FollowDto implements ExcelDto {

    private Integer idx;            // 고유번호
    private String memberUuid;      // 회원 UUID
    @Email
    @ExcelColumnName(headerName = "lang.follow.memberId")
    private String memberId;        // 회원 아이디
    private Long memberIdx;         // 회원 idx
    @ExcelColumnName(headerName = "lang.follow.memberNick")
    private String memberNick;      // 회원 닉네임
    private Integer memberState;    // 회원 탈퇴
    private String memberStateText; // 회원 탈퇴 상태값
    private String memberStateBg;   // 회원 탈퇴 배경값
    private String followUuid;      // 팔로우 된 회원 UUID
    @Email
    @ExcelColumnName(headerName = "lang.follow.followId")
    private String followId;        // 팔로우 아이디
    private Long followIdx;         // 팔로우 idx
    @ExcelColumnName(headerName = "lang.follow.followNick")
    private String followNick;      // 회원 닉네임
    private Integer followState;    // 팔로우 탈퇴
    private String followStateText; // 팔로우 탈퇴 상태값
    private String followStateBg;   // 팔로우 탈퇴 배경값
    @ExcelColumnName(headerName = "lang.follow.regdate")
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

    @Override
    public List<Object> mapToList() {
        return Arrays.asList(memberId, memberNick, followId, followNick, regDate);
    }
}
