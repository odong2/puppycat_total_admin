package com.architecture.admin.services.shopping.brand;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.shopping.brand.BrandDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService extends BaseService {

    private final BrandDaoSub brandDaoSub;

    /**
     * 상품 브랜드 목록
     * @return
     */
    public Map<Integer, List<BrandDto>> getBrandList() {
        List<BrandDto> brandList = brandDaoSub.getBrandList();

        return brandList.stream().collect(Collectors.groupingBy(BrandDto::getBrandGroupIdx, Collectors.toList()));
    }

    /**
     * 브랜드 그룹(초성) 목록
     * @return
     */
    public List<BrandDto> getBrandGroupList() {
        List<BrandDto> brandGroupList = brandDaoSub.getBrandGroupList();

        return brandGroupList;
    }

    /**
     * 쿠폰 적용 브랜드 검색
     *
     * @param searchDto searchWord searchType
     * @return list
     */
    public List<BrandDto> searchBrand(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = brandDaoSub.getBrandSearchCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        return brandDaoSub.getBrandSearchList(searchDto);
    }
}
