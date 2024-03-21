package com.architecture.admin.models.dao.wordcheck.pet;

import com.architecture.admin.models.dto.wordcheck.pet.PetWordCheckDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PetWordCheckDao {

    /*****************************************************
     * Delete
     ****************************************************/

    /**
     * contents 금칙어 등록
     *
     * @param petWordCheckDto
     * @return insertedIdx
     */
    int insert(PetWordCheckDto petWordCheckDto);

    /**
     * contents 금칙어 삭제
     *
     * @param petWordCheckDto idx
     * @return
     */
    int delete(PetWordCheckDto petWordCheckDto);

    /**
     * contents 금칙어 삭제 취소
     *
     * @param petWordCheckDto idx
     * @return
     */
    int deleteCancel(PetWordCheckDto petWordCheckDto);

}
