package com.architecture.admin.models.daosub.wordcheck.contents;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.contents.ContentsWordCheckDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContentsWordCheckDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 금칙어 목록
     *
     * @param searchDto
     * @return list
     */
    List<ContentsWordCheckDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);
}
