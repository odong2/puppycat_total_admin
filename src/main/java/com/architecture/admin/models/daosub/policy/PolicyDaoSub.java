package com.architecture.admin.models.daosub.policy;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.policy.PolicyDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PolicyDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 이용약관 목록
     *
     * @param searchDto
     * @return list
     */
    List<PolicyDto> getPolicyList(SearchDto searchDto);

    /**
     * 이용약관 상세 정보
     *
     * @param idx
     * @return
     */
    PolicyDto getViewPolicy(int idx);

    /**
     * 같은 카테고리내 현행중인 약관 있는지 체크
     *
     * @param policyDto
     * @return
     */
    int getCountCurrent(PolicyDto policyDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 카테고리 리스트 조회
     *
     * @return
     */
    List<PolicyDto> getPolicyMenuList(PolicyDto policyDto);

}
