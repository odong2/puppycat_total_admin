package com.architecture.admin.services.wordcheck.member;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.wordcheck.member.MemberWordCheckDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.wordcheck.member.MemberWordCheckDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


/*****************************************************
 * 회원 금칙어 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class MemberWordCheckService extends BaseService {
    private final MemberWordCheckDaoSub memberWordCheckDaoSub;
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
    public List<MemberWordCheckDto> getList(SearchDto searchDto){

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = memberWordCheckDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        List<MemberWordCheckDto> list = memberWordCheckDaoSub.getList(searchDto);

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
        List<MemberWordCheckDto> list = memberWordCheckDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, MemberWordCheckDto.class);
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
    public void stateText(List<MemberWordCheckDto> list) {
        for (MemberWordCheckDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(MemberWordCheckDto dto) {
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
        // 타입 (1 : 모두, 2: 닉네임, 3: 소개글)
        if (dto.getType() != null) {
            if (dto.getType() == 1) { // 모두
                dto.setTypeText(super.langMessage("lang.wordcheck.type.all"));
            } else if (dto.getType() == 2) { // 닉네임
                dto.setTypeText(super.langMessage("lang.wordcheck.type.member.nick"));
            } else if (dto.getType() == 3) { // 소개글
                dto.setTypeText(super.langMessage("lang.wordcheck.type.member.intro"));
            }
        }
    }
}
