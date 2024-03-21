package com.architecture.admin.models.daosub.restrain.partner;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.models.dto.restrain.member.MemberRestrainDto;
import com.architecture.admin.models.dto.restrain.partner.PartnerRestrainDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PartnerRestrainDaoSub {

    /**
     * 제재 코드 조회
     *
     * @return
     */
    List<PartnerRestrainDto> getTypeList();

    /**
     * 제재 파트너 카운트
     *
     * @param searchDto
     * @return
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 제재 파트너 목록
     *
     * @param searchDto
     * @return
     */
    List<PartnerRestrainDto> getList(SearchDto searchDto);

    /**
     * 제재 상태 조회
     *
     * @param partnerIdx shop_partner.idx
     * @return
     */
    PartnerDto getRestrainInfo(Integer partnerIdx);
}
