package com.architecture.admin.services.shopping.category;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.shopping.category.CategoryDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.category.CategoryDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService extends BaseService {

    private final CategoryDaoSub categoryDaoSub;

    /**
     * 상품 카테고리 목록
     * @return
     */
    public Map<Integer, List<CategoryDto>> getCategoryList() {
        List<CategoryDto> categoryList = categoryDaoSub.getCategoryList();

        /** treeview 구조 만들기 **/
        List<Integer> depthList = categoryList.stream().map(CategoryDto::getDepth).distinct().collect(Collectors.toList());
        Map<Integer, Map<Integer, List<CategoryDto>>> listMap = new HashMap<>();
        depthList.forEach((depth) -> {
            listMap.put(depth, categoryList.stream().filter(category -> category.getDepth() == depth).collect(Collectors.groupingBy(CategoryDto::getParent)));
        });

        //내림차순
        TreeMap<Integer, Map<Integer, List<CategoryDto>>> mapReverse = new TreeMap<>(Collections.reverseOrder());
        mapReverse.putAll(listMap);

        mapReverse.forEach((key, value) -> {
            if (key > 1) {
                for(Map.Entry<Integer, List<CategoryDto>> mapDto : mapReverse.get(key -1).entrySet()) {
                    for (CategoryDto dto : mapDto.getValue()) {
                        dto.setChildrenCategory(mapReverse.get(key).get(dto.getIdx()));
                    }
                }
            }
        });

        /** treeview 구조 만들기 **/

        return listMap.get(1);
    }


    /**
     * 상품 카테고리 하위 마지막 노드
     * @param categoryIdx
     * @return
     */
    public CategoryDto getCategoryLastNode(Integer categoryIdx) {

        return categoryDaoSub.getCategoryLastNode(categoryIdx);
    }

    /**
     * 쿠폰 적용 카테고리 검색
     *
     * @param searchDto searchWord searchType
     * @return list
     */
    public List<CategoryDto> searchCategory(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = categoryDaoSub.getCategorySearchCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        return categoryDaoSub.getCategorySearchList(searchDto);
    }

}
