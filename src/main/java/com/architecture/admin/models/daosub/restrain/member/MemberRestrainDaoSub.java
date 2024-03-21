package com.architecture.admin.models.daosub.restrain.member;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.member.MemberRestrainDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MemberRestrainDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 제재 회원 목록
     *
     * @param searchDto
     * @return list
     */
    List<MemberRestrainDto> getListMemberRestrain(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    List<MemberRestrainDto> getTypeList();
    List<MemberRestrainDto> getDateList();
    /**
     * 제재 사유 목록
     *
     * @return list
     */
    List<MemberRestrainDto> getCodeList();

    /**
     *  제재 목록 By memberIdx
     *
     * @param memberRestrainDto memberIdx
     * @return list
     */
    List<MemberRestrainDto> getListMemberRestrainByMemberIdx(MemberRestrainDto memberRestrainDto);

    /**
     * 제재 기간 가져오기
     *
     * @param memberRestrainDto idx
     * @return date
     */
    Integer getRestrainDate(MemberRestrainDto memberRestrainDto);

    /**
     * 제재 상세 내용
     * 
     * @param idx
     * @return
     */
    MemberRestrainDto getViewRestrain(Long idx);

}
