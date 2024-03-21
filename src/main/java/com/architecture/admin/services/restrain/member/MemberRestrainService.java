package com.architecture.admin.services.restrain.member;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.restrain.member.MemberRestrainDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.member.MemberRestrainDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/*****************************************************
 * 회원 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class MemberRestrainService extends BaseService {
    private final MemberRestrainDaoSub memberRestrainDaoSub;
    private final ExcelData excelData;
    @Value("${env.server}")
    private String server;  // 서버
    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 제재 상태 확인 날짜 계산
     *
     * @param memberRestrainDto state
     * @throws ParseException
     */
    public void checkDateState(MemberRestrainDto memberRestrainDto) throws ParseException {
        // state값이 1이고, 시작일과 종료일이 비어있지 않을 경우
        if (memberRestrainDto.getState() == 1 && memberRestrainDto.getStartDate() != null && memberRestrainDto.getEndDate() != null) {

            // 현재 시간 UTC 가져오기
            String nowDate = dateLibrary.getDatetime();
            // 로컬 시간으로 변경
            nowDate = dateLibrary.utcToLocalTime(nowDate);

            // 날짜 변환 string -> date
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateformat.setLenient(false);

            Date startDate = dateformat.parse(memberRestrainDto.getStartDate());
            Date endDate = dateformat.parse(memberRestrainDto.getEndDate());
            Date thisDate = dateformat.parse(nowDate);

            // 오늘 날짜가 시작날짜보다 전날이면 
            // 제재 시작시간 전이면
            if (thisDate.before(startDate)) {
                // 제재 대기
                memberRestrainDto.setStateDateText(super.langMessage("lang.restrain.member.wait"));
                memberRestrainDto.setStateDateBg("badge-warning");
            }// 제재 기간 이면
            else if (startDate.before(thisDate) && endDate.after(thisDate)) {
                // 제재중
                memberRestrainDto.setStateDateText(super.langMessage("lang.restrain.member.ing"));
                memberRestrainDto.setStateDateBg("badge-success");
            } else {
                // 제재 기간 만료
                memberRestrainDto.setStateDateText(super.langMessage("lang.restrain.member.stop"));
                memberRestrainDto.setStateDateBg("badge-danger");
            }
        } else {
            memberRestrainDto.setStateDateText("");
            memberRestrainDto.setStateDateBg("");
        }
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) throws ParseException {


        // 탈퇴 회원 조회
        List<MemberRestrainDto> listMember = memberRestrainDaoSub.getListMemberRestrain(searchDto);

        // 상태값 문자 변환
        if (!listMember.isEmpty()) {
            // 날짜 계산 state 변환
            checkDateState(listMember);

            // 문자변환
            stateText(listMember);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(listMember, MemberRestrainDto.class);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 회원 목록
     *
     * @param searchDto
     * @return 회원 리스트
     */
    public List<MemberRestrainDto> getListMember(SearchDto searchDto) throws ParseException {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = memberRestrainDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<MemberRestrainDto> listMember = memberRestrainDaoSub.getListMemberRestrain(searchDto);

        if (!listMember.isEmpty()) {
            // 날짜 계산 state 변환
            checkDateState(listMember);

            // 문자변환
            stateText(listMember);
        }
        return listMember;
    }

    /**
     * 회원 제재 목록 by memberIdx
     *
     * @param memberRestrainDto memberIdx
     * @return 회원 제재 목록
     */
    public List<MemberRestrainDto> getListMemberByMemberIdx(MemberRestrainDto memberRestrainDto) throws ParseException {
        // 회원 제재 리스트
        List<MemberRestrainDto> listMember = memberRestrainDaoSub.getListMemberRestrainByMemberIdx(memberRestrainDto);

        if (!listMember.isEmpty()) {
            // 날짜 계산 state 변환
            checkDateState(listMember);
            // 문자변환
            stateText(listMember);
        }
        return listMember;
    }

    public MemberRestrainDto getView(Long idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.RESTRAIN_IDX);
        }

        // 상세 정보
        return memberRestrainDaoSub.getViewRestrain(idx);
    }

    public List<MemberRestrainDto> getTypeList() {
        return memberRestrainDaoSub.getTypeList();
    }

    public List<MemberRestrainDto> getDateList() {
        return memberRestrainDaoSub.getDateList();
    }

    public List<MemberRestrainDto> getCodeList() {
        return memberRestrainDaoSub.getCodeList();
    }

    public Integer getRestrainDate(MemberRestrainDto memberRestrainDto) {
        return memberRestrainDaoSub.getRestrainDate(memberRestrainDto);
    }

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
    public void stateText(List<MemberRestrainDto> list) {
        for (MemberRestrainDto l : list) {
            stateText(l);
        }
    }

    /**
     * state변환 list
     *
     * @param list
     */
    public void checkDateState(List<MemberRestrainDto> list) throws ParseException {
        for (MemberRestrainDto l : list) {
            checkDateState(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(MemberRestrainDto dto) {
        // 상태 (0:삭제 1:정상 2:신청 3:반려)
        if (dto.getState() != null) {
            if (dto.getState() == 0) { // 삭제
                dto.setStateText(super.langMessage("lang.restrain.member.state.delete"));
                dto.setStateBg("badge-danger");
            } else if (dto.getState() == 1) { // 정상
                dto.setStateText(super.langMessage("lang.restrain.member.state.normal"));
                dto.setStateBg("badge-success");
            } else if (dto.getState() == 2) { // 신청
                dto.setStateText(super.langMessage("lang.restrain.member.state.apply"));
                dto.setStateBg("badge-warning");
            } else if (dto.getState() == 3) { // 반려
                dto.setStateText(super.langMessage("lang.restrain.member.state.reject"));
                dto.setStateBg("badge-danger");
            }
        }
        if (dto.getStartDate() == null) {
            dto.setStartDate("");
        }
        if (dto.getEndDate() == null) {
            dto.setEndDate("");
        }
    }
}
