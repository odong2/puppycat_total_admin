package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.OutMemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OutMemberDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 탈퇴 회원 전체 조회
     *
     * @param searchDto
     * @return
     */
    List<OutMemberDto> getListOutMember(SearchDto searchDto);

    /**
     * 탈퇴 회원 전체 카운트
     *
     * @param searchDto
     * @return
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 탈퇴 사유 조회하기
     *
     * @return List<OutMemberDto> 사유 코드 리스트
     */
    List<OutMemberDto> getOutCodeList();
}
