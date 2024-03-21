package com.architecture.admin.services.wordcheck.contents;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.wordcheck.contents.ContentsWordCheckDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.contents.ContentsWordCheckDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/*****************************************************
 * 콘텐츠 금칙어 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class ContentsWordCheckService extends BaseService {

    private final ContentsWordCheckDaoSub contentsWordcheckDaoSub;
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
    public List<ContentsWordCheckDto> getList(SearchDto searchDto){

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = contentsWordcheckDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        List<ContentsWordCheckDto> list = contentsWordcheckDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }
        return list;
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
        List<ContentsWordCheckDto> list = contentsWordcheckDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, ContentsWordCheckDto.class);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<ContentsWordCheckDto> list) {
        for (ContentsWordCheckDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(ContentsWordCheckDto dto) {
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
        // 타입 (1 : 모두, 2: 콘텐츠, 3: 댓글)
        if (dto.getType() != null) {
            if (dto.getType() == 1) { // 모두
                dto.setTypeText(super.langMessage("lang.wordcheck.type.all"));
            } else if (dto.getType() == 2) { // 콘텐츠
                dto.setTypeText(super.langMessage("lang.wordcheck.type.contents"));
            } else if (dto.getType() == 3) { // 댓글
                dto.setTypeText(super.langMessage("lang.wordcheck.type.comment"));
            }
        }
    }
}
