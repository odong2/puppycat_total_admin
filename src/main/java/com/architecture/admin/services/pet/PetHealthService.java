package com.architecture.admin.services.pet;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.pet.PetHealthDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.HealthDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetHealthService extends BaseService {

    private final PetHealthDaoSub petHealthDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 건강질환 리스트 조회
     *
     * @param searchDto
     * @return
     */
    public List<HealthDto> getHealthList(SearchDto searchDto) {

        // 건강질환 카운트
        int totalCnt = petHealthDaoSub.getHealthTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCnt, searchDto);
        searchDto.setPagination(pagination);

        // 건강 질환 리스트 조회
        List<HealthDto> healthList = petHealthDaoSub.getHealthList(searchDto);
        stateText(healthList);

        return healthList;
    }

    /**
     * 건강질환 전체 리스트 조회
     * @return
     */
    public List<HealthDto> getAllHealthList() {

        return petHealthDaoSub.getAllHealthList();
    }

    /**
     * 건강질환 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // get Lists
        List<HealthDto> healthList = petHealthDaoSub.getHealthList(searchDto);

        // 상태값 문자 변환
        stateText(healthList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(healthList, HealthDto.class);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/

    /**
     * 건강질환 정보
     *
     * @param idx
     * @return
     */
    public HealthDto getHealthInfo(Long idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        return petHealthDaoSub.getHealthInfo(idx);
    }

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<HealthDto> list) {
        for (HealthDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(HealthDto dto) {
        // 상태 (0 : 삭제, 1: 정상)
        if (dto.getState() != null) {
            if (dto.getState() == 0) { // 삭제
                dto.setStateText(super.langMessage("lang.pet.state.delete"));
                dto.setStateBg("badge-danger");
            } else if (dto.getState() == 1) { // 정상
                dto.setStateText(super.langMessage("lang.pet.state.normal"));
                dto.setStateBg("badge-success");
            }
        }
    }



}
