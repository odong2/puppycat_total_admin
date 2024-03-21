package com.architecture.admin.models.daosub.wordcheck.member;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.member.MemberWordCheckDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MemberWordCheckDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 금칙어 목록
     *
     * @param searchDto
     * @return list
     */
    List<MemberWordCheckDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);
}
