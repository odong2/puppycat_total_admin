package com.architecture.admin.services.member;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.member.OutMemberDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.OutMemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OutMemberService extends BaseService {

    private final OutMemberDaoSub outMemberDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 탈퇴 회원 목록
     *
     * @param searchDto 검색조건
     * @return 탈퇴 회원 list
     */
    @Transactional(readOnly = true)
    public List<OutMemberDto> getListOutMember(SearchDto searchDto) {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = outMemberDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 탈퇴회원리스트
        List<OutMemberDto> outListMember = outMemberDaoSub.getListOutMember(searchDto);

        if (!outListMember.isEmpty()) {
            // 문자변환
            stateText(outListMember);
        }
        return outListMember;
    }

    /**
     * 탈퇴 회원 엑셀 다운로드
     *
     * @param searchDto 검색조건
     * @return 탈퇴 회원 list
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 탈퇴 회원 조회
        List<OutMemberDto> outMemberList = outMemberDaoSub.getListOutMember(searchDto);
        
        // 상태값 문자 변환
        stateText(outMemberList);

        // 엑셀 데이터 변환
        return excelData.createExcelData(outMemberList, OutMemberDto.class);
    }

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 탈퇴 사유 리스트 가져오기
     *
     * @return List<OutMemberDto> 사유 코드 리스트
     */
    public List<OutMemberDto> getOutCodeList() {
        return outMemberDaoSub.getOutCodeList();
    }

    /*****************************************************
     *  SubFunction - Insert
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
    public void stateText(List<OutMemberDto> list) {

        for (OutMemberDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(OutMemberDto dto) {
        // 상태값
        if (dto.getState() != null) {
            if (dto.getState() == 3) { // 복구
                dto.setStateText(super.langMessage("lang.member.out.state.restore"));
                dto.setStateBg("bg-success");
            } else if (dto.getState() == 2) { // 탈퇴대기
                dto.setStateText(super.langMessage("lang.member.out.state.wait"));
                dto.setStateBg("bg-warning");
            } else if (dto.getState() == 1) { // 탈퇴확정
                dto.setStateText(super.langMessage("lang.member.out.state.complete"));
                dto.setStateBg("bg-danger");
            }
        }
        // 탈퇴확정일
        if (dto.getOutDate() == null) {
            dto.setOutDate("");
        }
        // 탈퇴 상세사유
        if (dto.getReason() == null) {
            dto.setReason("");
        }
        // 간편가입
        if (dto.getSimpleType() == null) { // 일반
            dto.setSimpleType(super.langMessage("lang.member.normal"));
        }

    }

}
