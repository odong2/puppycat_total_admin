package com.architecture.admin.models.daosub.shopping.category;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CategoryDaoSub {

    /**
     * 상품 카테고리 목록
     * @return
     */
    List<CategoryDto> getCategoryList();

    /**
     * 상품 카테고리 하위 마지막 노드
     * @param categoryIdx
     * @return
     */
    CategoryDto getCategoryLastNode(Integer categoryIdx);

    /**
     * 적용 카테고리 검색 카운트
     *
     * @param searchDto
     * @return
     */
    int getCategorySearchCount(SearchDto searchDto);

    /**
     * 적용 카테고리 검색 리스트
     *
     * @param searchDto
     * @return
     */
    List<CategoryDto> getCategorySearchList(SearchDto searchDto);

}
