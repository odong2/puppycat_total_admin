package com.architecture.admin.services.shopping.product;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.shopping.product.ProductMpDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.models.dto.shopping.product.ProductImgDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductMpService extends BaseService {

    private final ProductMpDaoSub productMpDaoSub;
    private final ThumborLibrary thumborLibrary;

    /**
     * MP 상품 목록
     * @param searchDto
     * @return
     */
    public List<ProductDto> getList(SearchDto searchDto) {

        // MP 상품 전체 수
        Integer totalCount = productMpDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // MP 상품 목록
        List<ProductDto> list = productMpDaoSub.getList(searchDto);

        for(ProductDto dto : list) {
            for(ProductImgDto imgDto : dto.getThumbnailList()) {
                imgDto.setUrl(thumborLibrary.getUploadedFullUrl( imgDto.getUrl(), "shop"));
            }
        }

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * MP 상품 상세
     * @param idx
     * @return
     */
    public ProductDto getProductMp(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.PRODUCT_IDX_ERROR);
        }

        // MP 상품 조회
        ProductDto dto = productMpDaoSub.getProductMp(idx);

        for(ProductImgDto imgDto : dto.getThumbnailList()) {
            imgDto.setUrl(thumborLibrary.getUploadedFullUrl( imgDto.getUrl(), "shop"));
        }

        return dto;

    }


    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<ProductDto> list) {
        for (ProductDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(ProductDto dto) {
        // 상태 (0 : 삭제 1: 정상)
        if (dto.getState() == 1) {  // 정상
            dto.setStateText(super.langMessage("lang.product.state.normal"));
            dto.setStateBg("badge-success");
        } else {
            dto.setStateText(super.langMessage("lang.product.state.delete"));
            dto.setStateBg("badge-danger");
        }
    }

}
