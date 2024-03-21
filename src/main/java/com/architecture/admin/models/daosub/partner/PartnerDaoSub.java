package com.architecture.admin.models.daosub.partner;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.partner.PartnerBankDto;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.models.dto.partner.PartnerImgDto;
import com.architecture.admin.models.dto.partner.PartnerManagerDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface PartnerDaoSub {

    /**
     * 파트너 리스트 카운트
     *
     * @param searchDto
     * @return
     */
    int getPartnerTotalCount(SearchDto searchDto);

    /**
     * 파트너 리스트
     *
     * @param searchDto
     * @return
     */
    List<PartnerDto> getPartnerList(SearchDto searchDto);

    /**
     * 파트너 상세 조회
     *
     * @param idx
     * @return
     */
    PartnerDto getPartnerDetailByIdx(Integer idx);

    /**
     * 회원가입시 업로드한 파트너 이미지 조회
     *
     * @param idx
     * @return
     */
    List<PartnerImgDto> getPartnerJoinImgListByIdx(int idx);

    /**
     * 파트너사 법인 리스트
     * 
     * @return main_idx  partner_code company_name
     */
    List<PartnerDto> getPartnerCompayNameList();
    /**
     * 파트너 거래 은행 정보 조회
     *
     * @param idx
     * @return
     */
    PartnerBankDto getPartnerBankInfoByIdx(int idx);

    /**
     * 파트너 매니저 리스트
     *
     * @param idx
     * @return
     */
    List<PartnerManagerDto> getPartnerManagerListByIdx(int idx);

    /**
     * 최근 반려 사유 조회
     *
     * @param idx
     * @return
     */
    String getPartnerRejectReason(Integer idx);

    /**
     * id 에 해당하는 카운트 가져오기: 신규 입력 시 중복되는 id가 있는지 체크에 사용
     *
     * @param id
     * @return
     */
    Integer getCountById(String id);
}
