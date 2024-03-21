package com.architecture.admin.models.daosub.pet;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PetDaoSub {

    /**
     * 반려동물 카운트
     *
     * @param searchDto
     * @return
     */
    int getTotalPetCount(SearchDto searchDto);

    /**
     * 반려동물 리스트
     *
     * @param searchDto
     * @return
     */
    List<PetDto> getPetList(SearchDto searchDto);

    /**
     * 반려동물 상세 보기
     *
     * @param searchDto : idx[펫 idx], memberIdx[회원 idx]
     * @return
     */
    PetDto getPetDetail(SearchDto searchDto);

    /**
     * 반려동물 알러지 정보 리스트
     *
     * @param idx
     * @return
     */
    List<String> getPetAllergyListByIdx(Long idx);

    /**
     * 반려동물 건강 정보 리스트
     *
     * @param idx
     * @return
     */
    List<String> getPetHealthListByIdx(Long idx);

    /**
     * 반려동물 정보 리스트 by PetUuid
     *
     * @param petUuidList
     * @return
     */
    List<PetDto> getPetInfoListByUuid(List<String> petUuidList);

}
