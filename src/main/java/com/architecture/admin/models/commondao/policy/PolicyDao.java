package com.architecture.admin.models.commondao.policy;

import com.architecture.admin.models.dto.policy.PolicyDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PolicyDao {

    /*****************************************************
     * Insert
     ****************************************************/
    /**
     * 이용약관 등록
     *
     * @param policyDto
     */
    void insertPolicy(PolicyDto policyDto);

    /**
     * 이용약관 제목 등록
     *
     * @param policyDto
     */
    void insertPolicyTitle(PolicyDto policyDto);

    /**
     * 이용약관 내용 등록
     *
     * @param policyDto
     */
    void insertPolicyDetail(PolicyDto policyDto);

    /*****************************************************
     * Update
     ****************************************************/
    /**
     * 이용약관 수정
     *
     * @param policyDto idx
     * @return affectedRow
     */
    Integer updatePolicy(PolicyDto policyDto);

    void updatePolicyCurrent(PolicyDto policyDto);

    /**
     * 이용약관 제목 수정
     *
     * @param policyDto
     */
    void updatePolicyTitle(PolicyDto policyDto);

    /**
     * 이용약관 내용 수정
     *
     * @param policyDto
     */
    void updatePolicyDetail(PolicyDto policyDto);

}
