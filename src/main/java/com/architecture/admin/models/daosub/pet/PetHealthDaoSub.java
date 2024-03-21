package com.architecture.admin.models.daosub.pet;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.HealthDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetHealthDaoSub {

    /**
     * 건강 질환 리스트
     *
     * @param searchDto
     * @return
     */
    List<HealthDto> getHealthList(SearchDto searchDto);

    /**
     * 건강 질환 카운트
     *
     * @param searchDto
     * @return
     */
    int getHealthTotalCount(SearchDto searchDto);

    /**
     * 건강질환 정보
     *
     * @param idx
     * @return
     */
    HealthDto getHealthInfo(Long idx);

    /**
     * 건강질환 전체 리스트 조회
     * @return
     */
    List<HealthDto> getAllHealthList();
}
