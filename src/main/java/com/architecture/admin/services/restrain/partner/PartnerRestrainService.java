package com.architecture.admin.services.restrain.partner;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.restrain.partner.PartnerRestrainDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.restrain.partner.PartnerRestrainDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerRestrainService extends BaseService {

    private final PartnerRestrainDaoSub partnerRestrainDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 파트너 제재 목록
     *
     * @param searchDto state searchType
     */
    public List<PartnerRestrainDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = partnerRestrainDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 제재 파트너 목록
        List<PartnerRestrainDto> list = partnerRestrainDaoSub.getList(searchDto);

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
    public Map<String, Object> excelDownload(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 제재 파트너 목록
        List<PartnerRestrainDto> listMember = partnerRestrainDaoSub.getList(searchDto);

        // 상태값 문자 변환
        if (!listMember.isEmpty()) {
            // 문자변환
            stateText(listMember);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(listMember, PartnerRestrainDto.class);
    }


    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 제재 코드 조회
     *
     * @return List<MemberRestrainDto>
     */
    public List<PartnerRestrainDto> getTypeList() {
        return partnerRestrainDaoSub.getTypeList();
    }

    /*****************************************************
     *  SubFunction - ETC
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<PartnerRestrainDto> list) {
        for (PartnerRestrainDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PartnerRestrainDto dto) {

        try {
            // state값이 1이고, 시작일과 종료일이 비어있지 않을 경우
            if (dto.getState() == 1 && dto.getStartDate() != null && dto.getEndDate() != null) {

                // 현재 시간 UTC 가져오기
                String nowDate = dateLibrary.getDatetime();
                // 로컬 시간으로 변경
                nowDate = dateLibrary.utcToLocalTime(nowDate);

                // 날짜 변환 string -> date
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                dateformat.setLenient(false);

                Date startDate = dateformat.parse(dto.getStartDate());
                Date endDate = dateformat.parse(dto.getEndDate());
                Date thisDate = dateformat.parse(nowDate);

                // 제재 시작시간 전이면
                if (thisDate.before(startDate)) {
                    // 제재 대기
                    dto.setStateText(super.langMessage("lang.restrain.partner.wait"));
                    dto.setStateBg("badge-warning");
                    dto.setState(2);
                }// 제재 기간 이면
                else if (startDate.equals(thisDate) || startDate.before(thisDate) && endDate.after(thisDate)) {
                    // 제재중
                    dto.setStateText(super.langMessage("lang.restrain.partner.ing"));
                    dto.setStateBg("badge-danger");
                    dto.setState(1);
                } else {
                    // 제재 기간 만료
                    dto.setStateText(super.langMessage("lang.restrain.partner.stop"));
                    dto.setStateBg("badge-success");
                    dto.setState(0);
                }
            } else {
                // 제재 중단
                dto.setStateText(super.langMessage("lang.restrain.partner.close"));
                dto.setStateBg("badge-light");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
