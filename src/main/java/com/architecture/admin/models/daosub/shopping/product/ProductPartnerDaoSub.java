package com.architecture.admin.models.daosub.shopping.product;


import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.models.dto.shopping.product.SearchProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductPartnerDaoSub {
    /**
     * 전체 카운트
     *
     * @param searchProductDto
     * @return
     */
    Long getTotalCount(SearchProductDto searchProductDto);

    /**
     * 파트너사 상품 목록
     *
     * @param searchProductDto
     * @return
     */
    List<ProductDto> getListProduct(SearchProductDto searchProductDto);

    /**
     * 파트너사 상품 상세
     *
     * @param idx
     * @return
     */
    ProductDto getViewProductPartner(Long idx);

    /**
     * 파트너 상품 반려 이유
     *
     * @param idx
     * @return
     */
    ProductDto getReturnReason(Long idx);

    /**
     * 파트너사 상품 제공고시
     *
     * @param idx
     * @return
     */
    List<AttributeSetDto> getPartnerAttribute(Long idx);

    /**
     * 현재 최저가 조회
     *
     * @param idx mpIdx
     * @return
     */
    Integer getProductMinPrice(Integer idx);
}
