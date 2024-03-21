package com.architecture.admin.services.wordcheck.pet;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.wordcheck.pet.PetWordCheckDao;
import com.architecture.admin.models.daosub.wordcheck.pet.PetWordCheckDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.pet.PetWordCheckDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/*****************************************************
 * 반려동물 금칙어 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class PetWordCheckService extends BaseService {

    private final PetWordCheckDao petWordCheckDao;
    private final PetWordCheckDaoSub petWordCheckDaoSub;
    private final AdminActionLogService adminActionLogService; // 관리자 action log 처리용
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 금칙어 목록
     *
     * @param searchDto
     * @return 금칙어 리스트
     */
    public List<PetWordCheckDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = petWordCheckDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        List<PetWordCheckDto> list = petWordCheckDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }
        return list;
    }

    /**
     * 닉네임 금칙어 등록
     *
     * @param petWordCheckDto type startDate endDate memo
     * @return insertedIdx
     */
    public Integer regist(PetWordCheckDto petWordCheckDto) {

        if (ObjectUtils.isEmpty(petWordCheckDto.getWord())) {
            // 금칙어를 입력해주세요
            throw new CustomException(CustomError.WORD_WORD_NULL);
        }
        if (ObjectUtils.isEmpty(petWordCheckDto.getType())) {
            // 금칙어 타입을 선택해주세요
            throw new CustomException(CustomError.WORD_TYPE_NULL);
        }

        Integer iResult = insert(petWordCheckDto);

        if (iResult > 0) {
            // 관리자 action log
            adminActionLogService.regist(petWordCheckDto, Thread.currentThread().getStackTrace());
        }

        return iResult;
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) throws ParseException {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // list
        List<PetWordCheckDto> list = petWordCheckDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, PetWordCheckDto.class);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/
    /**
     * 닉네임 금칙어 등록
     *
     * @param petWordCheckDto
     * @return insertedIdx
     */
    public Integer insert(PetWordCheckDto petWordCheckDto) {
        petWordCheckDto.setRegDate(dateLibrary.getDatetime());
        petWordCheckDto.setAdminIdx(super.getAdminIdx("idx"));

        return petWordCheckDao.insert(petWordCheckDto);
    }


    /*****************************************************
     *  SubFunction - Update
     ****************************************************/
    /**
     * 닉네임 금칙어 삭제 취소
     *
     * @param petWordCheckDto idx
     * @return
     */
    public int deleteCancel(PetWordCheckDto petWordCheckDto) {
        if (petWordCheckDto.getIdx() == null || petWordCheckDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        // 관리자 action log
        adminActionLogService.regist(petWordCheckDto, Thread.currentThread().getStackTrace());

        return petWordCheckDao.deleteCancel(petWordCheckDto);
    }

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/
    /**
     * 닉네임 금칙어 삭제
     *
     * @param petWordCheckDto idx
     * @return
     */
    public int delete(PetWordCheckDto petWordCheckDto) {
        if (petWordCheckDto.getIdx() == null || petWordCheckDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        // 관리자 action log
        adminActionLogService.regist(petWordCheckDto, Thread.currentThread().getStackTrace());

        return petWordCheckDao.delete(petWordCheckDto);
    }


    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<PetWordCheckDto> list) {
        for (PetWordCheckDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PetWordCheckDto dto) {
        // 상태 (0 : 삭제, 1: 정상, 2:제재 중, 3:제재 기간 만료)
        if (dto.getState() != null) {
            if (dto.getState() == 0) { // 삭제
                dto.setStateText(super.langMessage("lang.wordcheck.state.delete"));
                dto.setStateBg("badge-danger");
            } else if (dto.getState() == 1) { // 정상
                dto.setStateText(super.langMessage("lang.wordcheck.state.normal"));
                dto.setStateBg("badge-success");
            }
        }
        if (ObjectUtils.isEmpty(dto.getMemo())) {
            dto.setMemo("-");
        }
        // 타입 (1 : 모두, 2: 이름, 3: 품종)
        if (dto.getType() != null) {
            if (dto.getType() == 1) { // 모두
                dto.setTypeText(super.langMessage("lang.wordcheck.type.all"));
            } else if (dto.getType() == 2) { // 이름
                dto.setTypeText(super.langMessage("lang.wordcheck.type.pet.name"));
            } else if (dto.getType() == 3) { // 품종
                dto.setTypeText(super.langMessage("lang.wordcheck.type.pet.breed"));
            }
        }
    }
}
