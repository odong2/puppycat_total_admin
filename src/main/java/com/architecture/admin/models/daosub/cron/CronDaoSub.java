package com.architecture.admin.models.daosub.cron;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.cron.CronDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CronDaoSub {

    /**
     * 크론 카운트 가져오기
     *
     * @param searchDto 검색 조건
     * @return 크론 카운트
     */
    Integer getTotalCount(SearchDto searchDto);

    /**
     * 크론 리스트 가져오기
     *
     * @param searchDto 검색 조건
     * @return 크론 리스트
     */
    List<CronDto> getListCron(SearchDto searchDto);

}
