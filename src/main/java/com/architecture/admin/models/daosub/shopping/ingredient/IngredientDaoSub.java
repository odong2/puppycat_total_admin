package com.architecture.admin.models.daosub.shopping.ingredient;

import com.architecture.admin.models.dto.pet.AllergyDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IngredientDaoSub {


    /**
     * 단위 조회
     * @return
     */
    List<AllergyDto> getUnitList();
}
