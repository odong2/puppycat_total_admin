package com.architecture.admin.models.daosub.pet;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.BreedDto;
import com.architecture.admin.models.dto.pet.PetDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetBreedDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 반려동물 목록
     *
     * @return 반려동물 목록 ( pet_type )
     */
    List<PetDto> getPetTypeList();

    /**
     * 품종 정보 가져오기
     *
     * @param idx pet_breed.idx
     * @return
     */
    PetDto getBreedInfo(Long idx);


    /**
     * 품종 리스트
     *
     * @param typeIdx
     * @return
     */
    List<PetDto> getPetBreedList(int typeIdx);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);


    /**
     * 기타 품종 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalEtcCount(SearchDto searchDto);

    /**
     * 반려동물 품종 목록
     *
     * @param searchDto
     * @return list
     */
    List<BreedDto> getList(SearchDto searchDto);


    /**
     * 반려동물 기타 품종 목록
     *
     * @param searchDto
     * @return list
     */
    List<BreedDto> getEtcList(SearchDto searchDto);
}
