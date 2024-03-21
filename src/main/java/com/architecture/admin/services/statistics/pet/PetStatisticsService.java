package com.architecture.admin.services.statistics.pet;

import com.architecture.admin.models.dao.statistics.PetStatisticsDao;
import com.architecture.admin.models.daosub.statistics.PetStatisticsDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PetStatisticsService extends BaseService {
    private final PetStatisticsDao petStatisticsDao;
    private final PetStatisticsDaoSub petStatisticsDaoSub;


    /**
     * 알러지 통계
     *
     * @param searchDto
     * @return
     */
    public List<PetDto> getAllergy(SearchDto searchDto) {

        // 알러지 통계
        return petStatisticsDaoSub.getAllergy(searchDto);
    }

    /**
     * 품종별 알러지 통계
     *
     * @param searchDto
     * @return
     */
    public List<PetDto> getBreedAllergy(SearchDto searchDto) {

        // 알러지 통계
        return petStatisticsDaoSub.getBreedAllergy(searchDto);
    }

    /**
     * 품종 통계
     *
     * @param searchDto
     * @return
     */
    public List<PetDto> getBreedStat(SearchDto searchDto) {

        // 품종 통계
        return petStatisticsDaoSub.getBreedStat(searchDto);
    }

}
