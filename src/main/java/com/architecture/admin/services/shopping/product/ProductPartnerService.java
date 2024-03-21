package com.architecture.admin.services.shopping.product;


import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.shopping.product.ProductPartnerDaoSub;
import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import com.architecture.admin.models.dto.shopping.product.ProductDto;
import com.architecture.admin.models.dto.shopping.product.SearchProductDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductPartnerService extends BaseService {

    private final ProductPartnerDaoSub productPartnerDaoSub;
    private final ExcelData excelData;
    private final ThumborLibrary thumborLibrary;


    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 엑셀 다운로드
     *
     * @param searchProductDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchProductDto searchProductDto) {
        // 탈퇴 회원 조회
        List<ProductDto> listProduct = productPartnerDaoSub.getListProduct(searchProductDto);

        // 상태값 문자 변환
        if (!listProduct.isEmpty()) {
            // 문자변환
            stateText(listProduct);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(listProduct, ProductDto.class);
    }

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 파트너 상품 리스트
     *
     * @param searchProductDto
     * @return
     */
    public List<ProductDto> getListProduct(SearchProductDto searchProductDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchProductDto);

        // 목록 전체 count
        Long totalCount = productPartnerDaoSub.getTotalCount(searchProductDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(Math.toIntExact(totalCount), searchProductDto);
        searchProductDto.setPagination(pagination);

        // 파트너 상품 리스트
        List<ProductDto> list = productPartnerDaoSub.getListProduct(searchProductDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }


    /**
     * MP 상품 상세
     *
     * @param idx 파트너상품IDX
     * @return
     */
    public ProductDto getViewProductPartner(Long idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.PRODUCT_IDX_ERROR);
        }

        // MP 상품 조회
        ProductDto dto = productPartnerDaoSub.getViewProductPartner(idx);

        // MP 상품의 제공고시가 비어있으면 파트너사 제공고시 조회
        if (dto.getAttributeList().isEmpty()) {
            List<AttributeSetDto> partnerAttributeList = productPartnerDaoSub.getPartnerAttribute(idx);
            dto.setAttributeList(partnerAttributeList);
            dto.setPartnerAttributeUseState(1);
        }
        
        // MP상품IDX로 현재 파트너 최저가 조회
        Integer iProductMinPrice = productPartnerDaoSub.getProductMinPrice(dto.getIdx());
        dto.setNowMinPrice(iProductMinPrice);
        
        // 승인 반려 상품이면 반려 사유 조회 후 세팅
        if(dto.getState() == 9){
            ProductDto returnReasonInfo = productPartnerDaoSub.getReturnReason(idx);
            dto.setReturnReason(returnReasonInfo.getReturnReason());
            dto.setReturnReasonAdmin(returnReasonInfo.getReturnReasonAdmin());
        }

        // 문자변환
        stateText(dto);

        return dto;
    }
    /*****************************************************
     *  SubFunction - Insert
     ****************************************************/

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

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
        // 상태값 :: state :: 0:삭제/1:승인완료/10:임시저장/2:승인요청/9:승인반려/99:제재
        if (dto.getState() == 1) {
            dto.setStateText(super.langMessage("lang.product.partner.state.approval.completed"));
            dto.setStateBg("badge-success");
        } else if (dto.getState() == 10) {
            dto.setStateText(super.langMessage("lang.product.partner.state.temporary.storage"));
            dto.setStateBg("badge-secondary");
        } else if (dto.getState() == 2) {
            dto.setStateText(super.langMessage("lang.product.partner.state.request.approval"));
            dto.setStateBg("badge-info");
        } else if (dto.getState() == 9) {
            dto.setStateText(super.langMessage("lang.product.partner.state.refrain.approval"));
            dto.setStateBg("badge-dark");
        } else if (dto.getState() == 99) {
            dto.setStateText(super.langMessage("lang.product.partner.state.sanctions"));
            dto.setStateBg("badge-danger");
        } else {
            dto.setStateText(super.langMessage("lang.product.partner.state.delete"));
            dto.setStateBg("badge-danger");
        }

        // 공개여부 :: open_state 1:공개 0:비공개
        if (dto.getOpenState() == 1) {
            dto.setOpenStateText(super.langMessage("lang.product.partner.public"));
            dto.setOpenStateBg("badge-success");
        } else {
            dto.setOpenStateText(super.langMessage("lang.product.partner.private"));
            dto.setOpenStateBg("badge-danger");
        }

        // 노출여부(최저가여부) :: min_price_state 1:Y 0 :N
        if (dto.getMinPriceState() == 1) {
            dto.setMinPriceStateText(super.langMessage("lang.product.partner.state.y"));
            dto.setMinPriceStateBg("badge-success");
        } else {
            dto.setMinPriceStateText(super.langMessage("lang.product.partner.state.n"));
            dto.setMinPriceStateBg("badge-danger");
        }

        //포인트 지급 여부 :: benefit_state
        // 임시저장 아닐때만 설정
        if (dto.getState() != 10) {
            if (dto.getBenefitState() == 1) {
                dto.setBenefitStateText(super.langMessage("lang.product.partner.state.use"));
                dto.setBenefitStateBg("badge-success");
            } else {
                dto.setBenefitStateText(super.langMessage("lang.product.partner.state.unused"));
                dto.setBenefitStateBg("badge-danger");
            }
        } else {
            dto.setBenefitStateText("-");
            dto.setBenefitStateBg("badge-light");
        }

        // 최저가 설정 :: sync
        // 임시저장 아닐때만 설정
        if (dto.getState() != 10) {
            if (dto.getSync() == 1) {
                dto.setSyncText(super.langMessage("lang.product.partner.state.use"));
                dto.setSyncBg("badge-success");
            } else {
                dto.setSyncText(super.langMessage("lang.product.partner.state.unused"));
                dto.setSyncBg("badge-danger");
            }
        } else {
            dto.setSyncText("-");
            dto.setSyncBg("badge-light");
        }

        // 품절 알람 설정
        // 임시저장 아닐때만 설정
        if (dto.getState() != 10) {
            if (dto.getAlarm() == 1) {
                dto.setAlarmText(super.langMessage("lang.product.partner.state.use"));
                dto.setAlarmBg("badge-success");
            } else {
                dto.setAlarmText(super.langMessage("lang.product.partner.state.unused"));
                dto.setAlarmBg("badge-danger");
            }
        } else {
            dto.setAlarmText("-");
            dto.setAlarmBg("badge-light");
        }

        // 배송비 유형 1:무료 2:유료 3:조건부무료
        if (dto.getState() != 10) {
            if (dto.getPaymentType() != null) {
                if (dto.getPaymentType() == 1) {
                    dto.setPaymentTypeText(super.langMessage("lang.product.partner.cost.free"));
                } else if (dto.getPaymentType() == 2) {
                    dto.setPaymentTypeText(super.langMessage("lang.product.partner.cost.pay"));
                } else if (dto.getPaymentType() == 3) {
                    dto.setPaymentTypeText(super.langMessage("lang.product.partner.cost.conditional.free"));
                }
            }
        } else {
            dto.setPaymentTypeText("-");
        }

        // 묶음배송 여부
        if (dto.getState() != 10) {
            if (dto.getIsCombine() != null) {
                if (dto.getIsCombine() == 1) {
                    dto.setIsCombineText(super.langMessage("lang.product.partner.state.possible"));
                    dto.setIsCombineBg("badge-success");
                } else {
                    dto.setIsCombineText(super.langMessage("lang.product.partner.state.impossible"));
                    dto.setIsCombineBg("badge-danger");
                }
            }
        } else {
            dto.setIsCombineText("-");
            dto.setIsCombineBg("badge-light");
        }

        // 배송정보가 비어있지 않다면
        if (dto.getDeliveryList() != null) {

            // 배송비 데이터 세팅
            if (dto.getDeliveryList().getPaymentType() == 1) {
                // 배송비 0 으로 set
                dto.getDeliveryList().setBasicFee(0);
            }

            // 당일 배송 시간 데이터 세팅
            if (dto.getDeliveryList().getDeadline() == 1) {
                String[] timeArray = dto.getDeliveryList().getSameDayDeliveryTime().split(":");

                dto.getDeliveryList().setSameDayDeliveryTimeHour(timeArray[0]);
                dto.getDeliveryList().setSameDayDeliveryTimeMinute(timeArray[1]);
            }

            // 배송 속성
            if (!ObjectUtils.isEmpty(dto.getDeliveryList().getProperty())) {
                if (dto.getDeliveryList().getProperty() == 1) {
                    dto.getDeliveryList().setPropertyText(super.langMessage("lang.product.partner.basic.delivery"));
                } else if (dto.getDeliveryList().getProperty() == 2) {
                    dto.getDeliveryList().setPropertyText(super.langMessage("lang.product.partner.dawn.delivery"));
                } else if (dto.getDeliveryList().getProperty() == 3) {
                    dto.getDeliveryList().setPropertyText(super.langMessage("lang.product.partner.express.delivery"));
                }
            }

            // 배송비 유형
            if (!ObjectUtils.isEmpty(dto.getDeliveryList().getPaymentType())) {
                if (dto.getDeliveryList().getPaymentType() == 1) {
                    dto.getDeliveryList().setPaymentTypeText(super.langMessage("lang.product.partner.cost.free"));
                } else if (dto.getDeliveryList().getPaymentType() == 2) {
                    dto.getDeliveryList().setPaymentTypeText(super.langMessage("lang.product.partner.cost.pay"));
                } else if (dto.getDeliveryList().getPaymentType() == 3) {
                    dto.getDeliveryList().setPaymentTypeText(super.langMessage("lang.product.partner.cost.conditional.free"));
                }
            }

            // 묶음 배송
            if (!ObjectUtils.isEmpty(dto.getDeliveryList().getIsCombine())) {
                if (dto.getDeliveryList().getIsCombine() == 1) {
                    dto.getDeliveryList().setIsCombineText(super.langMessage("lang.product.partner.possible"));
                } else {
                    dto.getDeliveryList().setIsCombineText(super.langMessage("lang.product.partner.impossible"));
                }
            }

            // 도서산관 배송
            if (!ObjectUtils.isEmpty(dto.getDeliveryList().getIsIsland())) {
                if (dto.getDeliveryList().getIsIsland() == 1) {
                    dto.getDeliveryList().setIsIslandText(super.langMessage("lang.product.partner.possible"));
                } else {
                    dto.getDeliveryList().setIsIslandText(super.langMessage("lang.product.partner.impossible"));
                }
            }
        }
    }
}
