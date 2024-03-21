package com.architecture.admin.models.daosub.shopping.brand;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BrandDaoSub {

    /**
     * 상품 브랜드 목록
     * @return
     */
    List<BrandDto> getBrandList();

    /**
     * 브랜드 그룹(초성) 목록
     * @return
     */
    List<BrandDto> getBrandGroupList();

    /**
     * 적용 카테고리 검색 카운트
     *
     * @param searchDto
     * @return
     */
    int getBrandSearchCount(SearchDto searchDto);

    /**
     * 적용 카테고리 검색 리스트
     *
     * @param searchDto
     * @return
     */
    List<BrandDto> getBrandSearchList(SearchDto searchDto);

}
