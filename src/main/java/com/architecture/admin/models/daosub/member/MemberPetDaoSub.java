package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.pet.PetDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberPetDaoSub {

    /**
     * 펫 리스트 정보 조회
     *
     * @param petUuidList
     * @return
     */
    List<PetDto> getPetInfoList(List<String> petUuidList);
}
