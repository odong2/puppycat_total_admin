package com.architecture.admin.services.pet;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.pet.PetAllergyDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.AllergyDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetAllergyService extends BaseService {

    private final PetAllergyDaoSub petAllergyDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 알러지 리스트 조회
     *
     * @param searchDto
     * @return
     */
    public List<AllergyDto> getAllergyList(SearchDto searchDto) {

        // 알러지 카운트
        int totalCnt = petAllergyDaoSub.getAllergyTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCnt, searchDto);
        searchDto.setPagination(pagination);

        // 알러지 리스트 조회
        List<AllergyDto> allergyList = petAllergyDaoSub.getAllergyList(searchDto);
        stateText(allergyList);

        return allergyList;
    }

    /**
     * 알러지 전체 리스트 조회
     * @return
     */
    public List<AllergyDto> getAllAllergyList() {
        return petAllergyDaoSub.getAllAllergyList();
    }

    /**
     * 알러지 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // get Lists
        List<AllergyDto> allergyList = petAllergyDaoSub.getAllergyList(searchDto);

        // 상태값 문자 변환
        stateText(allergyList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(allergyList, AllergyDto.class);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/

    /**
     * 알러지 정보
     *
     * @param idx
     * @return
     */
    public AllergyDto getAllergyInfo(Long idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        return petAllergyDaoSub.getAllergyInfo(idx);
    }

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<AllergyDto> list) {
        for (AllergyDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(AllergyDto dto) {
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
