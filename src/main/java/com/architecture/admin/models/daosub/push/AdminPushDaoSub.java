package com.architecture.admin.models.daosub.push;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.push.AdminPushDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface AdminPushDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 푸시 목록
     *
     * @param searchDto
     * @return list
     */
    List<AdminPushDto> getList(SearchDto searchDto);

    /**
     * state가 1인 워커 카운트
     * 
     * @return
     */
    Integer getWorkerCheck();


    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

}
