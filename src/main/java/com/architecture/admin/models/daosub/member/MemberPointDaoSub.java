package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberPointDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MemberPointDaoSub {

    /**
     * 회원 포인트 조회
     *
     * @param memberUuid : 회원 uuid
     * @return : point, save_point, use_point, expire_point
     */
    MemberPointDto getMemberPoint(String memberUuid);

    /**
     * 상품 주문 번호 체크
     *
     * @param memberPointDto : productOrderId, memberUuid
     * @return : count
     */
    int checkProductOrderId(MemberPointDto memberPointDto);

    /**
     * 포인트 리스트 카운트
     *
     * @param searchDto : type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return : totalCount
     */
    int getPointTotalCnt(SearchDto searchDto);

    /**
     * 포인트 리스트
     *
     * @param searchDto : type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return : list
     */
    List<MemberPointDto> getPointList(SearchDto searchDto);
}
