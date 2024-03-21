package com.architecture.admin.services.pet;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.pet.PetBreedDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.pet.BreedDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PetBreedService extends BaseService {

    private final PetBreedDaoSub petBreedDaoSub;
    private final ExcelData excelData;


    /*****************************************************
     *  Modules
     ****************************************************/


    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 반려동물 종류 리스트
     *
     * @return 반려동물 종류 ex. 강아지 / 고양이
     */
    public List<PetDto> getPetTypeList() {
        return petBreedDaoSub.getPetTypeList();
    }

    /**
     * 반려동물 품종 정보
     *
     * @param idx
     * @return
     */
    public PetDto getBreedInfo(Long idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        return petBreedDaoSub.getBreedInfo(idx);
    }

    /**
     * 견종별 품종 리스트
     *
     * @param typeIdx
     * @return
     */
    public List<PetDto> getPetBreedList(int typeIdx) {
        if (typeIdx < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        return petBreedDaoSub.getPetBreedList(typeIdx);

    }

    /**
     * 품종 리스트
     *
     * @param searchDto
     * @return
     */
    public List<BreedDto> getList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = petBreedDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 품종 리스트
        List<BreedDto> list = petBreedDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }
        return list;
    }

    /**
     * 기타 품종 리스트
     *
     * @param searchDto
     * @return
     */
    public List<BreedDto> getEtcList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = petBreedDaoSub.getTotalEtcCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<BreedDto> list = petBreedDaoSub.getEtcList(searchDto);

        return list;
    }

    /**
     * 품종 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // get Lists
        List<BreedDto> breedList = petBreedDaoSub.getList(searchDto);

        // 상태값 문자 변환
        stateText(breedList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(breedList, BreedDto.class);
    }

    /**
     * 기타 품종 엑셀 다운로드
     * @param searchDto
     * @return
     */
    public Map<String, Object> etcExcelDownload(SearchDto searchDto) {
        // get Lists
        List<BreedDto> breedList = petBreedDaoSub.getEtcList(searchDto);

        // 상태값 문자 변환
        stateText(breedList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(breedList, BreedDto.class);
    }

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<BreedDto> list) {
        for (BreedDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(BreedDto dto) {
        // 상태 (0 : 삭제, 1: 정상, 2:제재 중, 3:제재 기간 만료)
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
