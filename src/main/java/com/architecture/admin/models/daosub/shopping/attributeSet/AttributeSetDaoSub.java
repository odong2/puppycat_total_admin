package com.architecture.admin.models.daosub.shopping.attributeSet;

import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AttributeSetDaoSub {
    List<AttributeSetDto> getAttributeSetList();
}
