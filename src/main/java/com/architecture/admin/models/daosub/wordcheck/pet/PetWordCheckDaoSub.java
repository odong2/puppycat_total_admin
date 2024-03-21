package com.architecture.admin.models.daosub.wordcheck.pet;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.pet.PetWordCheckDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetWordCheckDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 금칙어 목록
     *
     * @param searchDto
     * @return list
     */
    List<PetWordCheckDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);
}
