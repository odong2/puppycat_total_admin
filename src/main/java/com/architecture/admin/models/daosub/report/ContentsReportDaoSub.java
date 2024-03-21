package com.architecture.admin.models.daosub.report;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.report.ContentsReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContentsReportDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 게시물 신고 목록
     *
     * @param searchDto 검색조건, pagination
     * @return list
     */
    List<ContentsReportDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto 검색조건
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * idx 검색
     *
     * @param idxList 검색조건
     * @return count
     */
    List<Long> getChkIdxList(List<Long> idxList);

}
