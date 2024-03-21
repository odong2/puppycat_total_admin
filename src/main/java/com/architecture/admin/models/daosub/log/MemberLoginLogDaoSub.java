package com.architecture.admin.models.daosub.log;


import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/*****************************************************
 * member access log
 ****************************************************/

@Repository
@Mapper
public interface MemberLoginLogDaoSub {
    /*****************************************************
     * Select
     ****************************************************/

    /**
     * 회원 활동 내역 최근 20개 리스트 by memberUuid
     *
     * @param memberDto
     * @return
     */
    List<MemberDto> getLoginLog(MemberDto memberDto);
}
