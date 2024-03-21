package com.architecture.admin.services.bank;

import com.architecture.admin.models.daosub.bank.BankDaoSub;
import com.architecture.admin.models.dto.bank.BankDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService extends BaseService {

    private final BankDaoSub bankDaoSub;

    /**
     * 등록된 은행 조회
     *
     * @return 은행 리스트
     */
    public List<BankDto> getBankList() {

        return bankDaoSub.getBankList();
    }
}
