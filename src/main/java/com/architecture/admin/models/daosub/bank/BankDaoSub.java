package com.architecture.admin.models.daosub.bank;

import com.architecture.admin.models.dto.bank.BankDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BankDaoSub {

    /**
     * 은행 리스트 조회
     *
     * @return
     */
    List<BankDto> getBankList();
}
