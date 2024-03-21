package com.architecture.admin.models.daosub.product;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductDaoSub {

    /**
     * 상품 조회 By product.idx List
     *
     * @param list
     * @return
     */
    List<ProductDto> getProductListByIdx(List<Integer> list);

    /**
     * 적용 상품 검색 카운트
     *
     * @param searchDto
     * @return
     */
    int getProductSearchCount(SearchDto searchDto);

    /**
     * 적용 상품 검색 리스트
     *
     * @param searchDto
     * @return
     */
    List<ProductDto> getProductSearchList(SearchDto searchDto);

}
