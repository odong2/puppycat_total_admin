package com.architecture.admin.models.daosub.statistics;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetStatisticsDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 알러지 통계
     *
     * @param searchDto
     * @return list
     */
    List<PetDto> getAllergy(SearchDto searchDto);

    /**
     * 품종별 알러지 통계
     *
     * @param searchDto
     * @return
     */
    List<PetDto> getBreedAllergy(SearchDto searchDto);

    /**
     * 품종 통계
     *
     * @param searchDto
     * @return list
     */
    List<PetDto> getBreedStat(SearchDto searchDto);

}
