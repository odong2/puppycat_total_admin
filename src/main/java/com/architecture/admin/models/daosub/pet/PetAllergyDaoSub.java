package com.architecture.admin.models.daosub.pet;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetAllergyDaoSub {

    /**
     * 알러지 리스트
     *
     * @param searchDto
     * @return
     */
    List<AllergyDto> getAllergyList(SearchDto searchDto);

    /**
     * 알러지 카운트
     *
     * @param searchDto
     * @return
     */
    int getAllergyTotalCount(SearchDto searchDto);

    /**
     * 알러지 정보
     *
     * @param idx
     * @return
     */
    AllergyDto getAllergyInfo(Long idx);

    /**
     * 알러지 전체 리스트 조회
     * @return
     */
    List<AllergyDto> getAllAllergyList();
}
