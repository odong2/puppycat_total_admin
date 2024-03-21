package com.architecture.admin.models.daosub.shopping.product;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;

import java.util.List;

public interface ProductMpDaoSub {

    /**
     * MP 상품 전체 수
     * @param searchDto
     * @return
     */
    Integer getTotalCount(SearchDto searchDto);

    /**
     * MP 상품 목록
     * @param searchDto
     * @return
     */
    List<ProductDto> getList(SearchDto searchDto);

    /**
     * MP 상품 상세
     * @param idx
     * @return
     */
    ProductDto getProductMp(Integer idx);
}
