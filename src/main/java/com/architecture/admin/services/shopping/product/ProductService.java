package com.architecture.admin.services.shopping.product;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.product.ProductDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductDaoSub productDaoSub;

    /**
     * 쿠폰 적용 상품 검색
     *
     * @param searchDto searchWord searchType
     * @return list
     */
    public List<ProductDto> searchProduct(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = productDaoSub.getProductSearchCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        return productDaoSub.getProductSearchList(searchDto);
    }
}
