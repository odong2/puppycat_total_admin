package com.architecture.admin.models.dto.block;

import com.architecture.admin.libraries.excel.ExcelColumnName;
import com.architecture.admin.libraries.excel.ExcelFileName;
import com.architecture.admin.models.dto.excel.ExcelDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ExcelFileName(filename = "lang.block.member.title.list")
public class BlockMemberDto implements ExcelDto {
    @ExcelColumnName(headerName = "lang.block.member.idx")
    private Long idx; // 일련번호
    private String memberUuid; // 회원 UUID
    private String blockUuid; // 차단대상 UUID
    private Long memberIdx; // 회원 idx
    private Integer state; // 차단 상태(0:해제, 1:차단)
    @ExcelColumnName(headerName = "lang.block.member.id")
    private String memberId; // 회원 id
    @ExcelColumnName(headerName = "lang.block.member.nick")
    private String memberNick; // 회원 nick
    private Integer memberState; // 회원 탈퇴
    private String memberStateText; // 회원 탈퇴 상태값
    private String memberStateBg; // 회원 탈퇴 배경값
    @ExcelColumnName(headerName = "lang.block.member.blocked.id")
    private String blockId; // 차단대상 id
    private Long blockIdx; // 차단대상 idx
    @ExcelColumnName(headerName = "lang.block.member.blocked.nick")
    private String blockNick; // 차단대상 nick
    private Integer blockState; // 차단대상 탈퇴
    private String blockStateText; // 차단대상 탈퇴 상태값
    private String blockStateBg; // 차단대상 탈퇴 배경값
    @ExcelColumnName(headerName = "lang.block.member.regdate")
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존
    
    @Override
    public List<Object> mapToList() {
        return Arrays.asList(idx, memberId, memberNick, blockId, blockNick, regDate);
    }
}
