package com.architecture.admin.services.shopping.ingredient;

import com.architecture.admin.models.daosub.shopping.ingredient.IngredientDaoSub;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService extends BaseService {

    private final IngredientDaoSub ingredientDaoSub;


    /**
     * 단위 조회
     * @return
     */
    public List<AllergyDto> getUnitList() {
        return ingredientDaoSub.getUnitList();
    }
}
