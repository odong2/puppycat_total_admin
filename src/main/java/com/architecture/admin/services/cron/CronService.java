package com.architecture.admin.services.cron;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.cron.CronDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.cron.CronDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CronService extends BaseService {

    private final CronDaoSub cronDaoSub;

    /**
     * 크론 목록
     *
     * @param searchDto 검색 조건
     * @return 크론 리스트
     */
    public List<CronDto> getCronList(SearchDto searchDto){

        // 목록 전체 count
        Integer totalCount = cronDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 크론 리스트
        List<CronDto> listCron = cronDaoSub.getListCron(searchDto);

        if (!listCron.isEmpty()) {
            // 문자변환
            stateText(listCron);
        }

        return listCron;

    }

    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<CronDto> list) {
        for (CronDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(CronDto dto) {
        if (dto.getState() != null) {
            // 상태 (0 : 비활성 1: 활성)
            if (dto.getState() == 1) { // 활성
                dto.setStateText(super.langMessage("lang.cron.state.normal"));
                dto.setStateBg("badge-success");
            } else { // 비활성
                dto.setStateText(super.langMessage("lang.cron.state.delete"));
                dto.setStateBg("badge-danger");
            }
        }
    }

}
