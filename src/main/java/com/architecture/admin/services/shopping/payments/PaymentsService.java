package com.architecture.admin.services.shopping.payments;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.shopping.payments.PaymentsDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.payments.PaymentsDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentsService extends BaseService {

    private final PaymentsDaoSub paymentsDaoSub;
    private final ExcelData excelData;

    /**
     * 결제 목록
     * @param searchDto
     * @return
     */
    public List<PaymentsDto> getList(SearchDto searchDto) {

        // 결제 전체 수
        Integer totalCount = paymentsDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 결제 목록
        List<PaymentsDto> list = paymentsDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) throws ParseException {

        // 결제 목록
        List<PaymentsDto> list = paymentsDaoSub.getList(searchDto);

        // 상태값 문자 변환
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, PaymentsDto.class);
    }


    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<PaymentsDto> list) {
        for (PaymentsDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PaymentsDto dto) {
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
