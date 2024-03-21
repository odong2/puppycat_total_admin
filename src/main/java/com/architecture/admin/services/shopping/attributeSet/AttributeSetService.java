package com.architecture.admin.services.shopping.attributeSet;

import com.architecture.admin.models.daosub.shopping.attributeSet.AttributeSetDaoSub;
import com.architecture.admin.models.dto.shopping.attributeSet.AttributeSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeSetService {

    private final AttributeSetDaoSub attributeSetDaoSub;

    public Map<Integer, List<AttributeSetDto>> getAttributeSetList() {
        List<AttributeSetDto> attributeSetList =  attributeSetDaoSub.getAttributeSetList();

        List<Integer> attributeIdxList = attributeSetList.stream().map(AttributeSetDto::getIdx).distinct().collect(Collectors.toList());
        Map<Integer, List<AttributeSetDto>> attributeSetListMap = new HashMap<>();

        attributeIdxList.forEach((attributeIdx) -> {
            attributeSetListMap.put(attributeIdx, attributeSetList.stream().filter(attribute -> attribute.getIdx() == attributeIdx).collect(Collectors.toList()));
        });

        return attributeSetListMap;
    }
}
