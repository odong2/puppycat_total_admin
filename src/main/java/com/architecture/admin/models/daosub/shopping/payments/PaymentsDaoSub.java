package com.architecture.admin.models.daosub.shopping.payments;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.payments.PaymentsDto;

import java.util.List;

public interface PaymentsDaoSub {

    /**
     * 결제 전체 수
     * @param searchDto
     * @return
     */
    Integer getTotalCount(SearchDto searchDto);

    /**
     * 결제 목록
     * @param searchDto
     * @return
     */
    List<PaymentsDto> getList(SearchDto searchDto);
}
