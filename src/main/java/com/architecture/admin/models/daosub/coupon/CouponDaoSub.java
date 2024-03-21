package com.architecture.admin.models.daosub.coupon;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.coupon.CouponDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CouponDaoSub {

    /**
     * 쿠폰 전체 카운트
     *
     * @param searchDto
     * @return
     */
    int getCouponTotalCount(SearchDto searchDto);

    /**
     * 쿠폰 리스트
     *
     * @param searchDto
     * @return
     */
    List<CouponDto> getCouponList(SearchDto searchDto);

    /**
     * 쿠폰 상세
     *
     * @param couponDto idx
     * @return
     */
    CouponDto getCouponView(CouponDto couponDto);

    /**
     * 쿠폰 발급 방식 리스트
     *
     * @return
     */
    List<CouponDto> getCouponTypeList();

    /**
     * 쿠폰 사용 범위 리스트
     *
     * @return
     */
    List<CouponDto> getCouponRangeList();

    /**
     * 타겟 쿠폰 회원 조회
     *
     * @param couponDto
     * @return
     */
    List<MemberDto> getCouponMemberMapping(CouponDto couponDto);

    /**
     * 적용 상품 매핑 조회
     *
     * @param couponDto
     * @return
     */
    List<CouponDto> getCouponProductMapping(CouponDto couponDto);

    /**
     * 적용 카테고리 매핑 조회
     *
     * @param couponDto
     * @return
     */
    List<CouponDto> getCouponCategoryMapping(CouponDto couponDto);

    /**
     * 적용 브랜드 매핑 조회
     *
     * @param couponDto
     * @return
     */
    List<CouponDto> getCouponBrandMapping(CouponDto couponDto);

}
