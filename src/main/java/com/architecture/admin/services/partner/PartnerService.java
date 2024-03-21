package com.architecture.admin.services.partner;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.partner.PartnerDaoSub;
import com.architecture.admin.models.daosub.restrain.partner.PartnerRestrainDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.partner.PartnerBankDto;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.models.dto.partner.PartnerImgDto;
import com.architecture.admin.models.dto.partner.PartnerManagerDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService extends BaseService {

    private final PartnerDaoSub partnerDaoSub;
    private final PartnerRestrainDaoSub partnerRestrainDaoSub;
    private final ExcelData excelData;

    @Value("${partner.state.leave}")
    private int PARTNER_STATE_LEAVE;           // 탈퇴
    @Value("${partner.state.normal}")
    private int PARTNER_STATE_NORMAL;          // 정상 상태
    @Value("${partner.state.additional.information}")
    private int PARTNER_STATE_ADDITIONAL_INFO; // 추가정보 입력 상태
    @Value("${partner.state.ready}")
    private int PARTNER_STATE_READY;           // 심사 대기중 상태
    @Value("${partner.state.judge}")
    private int PARTNER_STATE_JUDGE;           // 심사중
    @Value("${partner.state.judge.back}")
    private int PARTNER_STATE_JUDGE_BACK;      // 반려
    @Value("${partner.state.retrieve}")
    private int PARTNER_STATE_RETRIEVE;        // 회수
    @Value("${id.length.min}")
    private int ID_LENGTH_MIN; // 아이디 최소 수
    @Value("${id.length.max}")
    private int ID_LENGTH_MAX; // 아이디 최대 수


    /*****************************************************
     *  Module
     ****************************************************/
    /**
     * 파트너 리스트 조회
     *
     * @param searchDto
     * @return
     */
    public List<PartnerDto> getPartnerList(SearchDto searchDto) {

        // 날짜 유효성
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = partnerDaoSub.getPartnerTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 파트너 리스트
        List<PartnerDto> list = partnerDaoSub.getPartnerList(searchDto);

        // 문자 변환
        stateText(list);

        return list;
    }

    /**
     * 파트너 상세 조회
     *
     * @param idx
     * @return
     */
    public PartnerDto getPartnerDetail(Integer idx) {
        PartnerDto partnerDto = partnerDaoSub.getPartnerDetailByIdx(idx);

        // 반려인 경우
        if (partnerDto.getState() == PARTNER_STATE_JUDGE_BACK) {
            String reason = getPartnerRejectReason(idx); // 반려 사유 조회
            partnerDto.setReason(reason);
        }

        // 대표자 연락처
        String presidentTel = partnerDto.getPresidentTel();

        if (!ObjectUtils.isEmpty(presidentTel)) {
            String[] presidentTelArray = presidentTel.split("-");
            partnerDto.setPresidentTel1(presidentTelArray[0]);
            partnerDto.setPresidentTel2(presidentTelArray[1]);

            if (presidentTelArray.length == 3) {
                partnerDto.setPresidentTel3(presidentTelArray[2]);
            }
        }

        stateText(partnerDto);

        return partnerDto;
    }

    /**
     * 파트너 리스트 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // get Lists
        List<PartnerDto> partnerList = partnerDaoSub.getPartnerList(searchDto);

        // 상태값 문자 변환
        stateText(partnerList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(partnerList, PartnerDto.class);
    }

    /*****************************************************
     *  SubFunction - SELECT
     ****************************************************/
    /**
     * 파트너 이미지 리스트 조회
     *
     * @param idx
     * @return
     */
    public List<PartnerImgDto> getPartnerImgList(Integer idx) {

        return partnerDaoSub.getPartnerJoinImgListByIdx(idx);
    }

    /**
     * 파트너사 법인 리스트
     *
     * @return
     */
    public List<PartnerDto> getPartnerCompayNameList(){
        return partnerDaoSub.getPartnerCompayNameList();
    }

    /**
     * 파트너 거래 은행 정보 조회
     *
     * @param idx
     * @return
     */
    public PartnerBankDto getPartnerBankInfo(Integer idx) {
        PartnerBankDto partnerBankDto = partnerDaoSub.getPartnerBankInfoByIdx(idx);

        if (ObjectUtils.isEmpty(partnerBankDto)) {
            partnerBankDto = new PartnerBankDto();
        }

        return partnerBankDto;
    }

    /**
     * 파트너 매니저 리스트 조회
     *
     * @param idx
     * @return
     */
    public List<PartnerManagerDto> getPartnerManagerList(Integer idx) {

        List<PartnerManagerDto> managerList = partnerDaoSub.getPartnerManagerListByIdx(idx);

        if (!ObjectUtils.isEmpty(managerList)) {
            // 전화번호 set
            for (PartnerManagerDto manager : managerList) {
                String phone = manager.getPhone();

                String[] phoneArray = phone.split("-");
                manager.setPhone1(phoneArray[0]);
                manager.setPhone2(phoneArray[1]);
                manager.setPhone3(phoneArray[2]);

                String tel = manager.getTel();

                if (!ObjectUtils.isEmpty(tel)) {
                    String[] telArray = tel.split("-");
                    manager.setTel1(telArray[0]);
                    manager.setTel2(telArray[1]);

                    if (telArray.length == 3) {
                        manager.setTel3(telArray[2]);
                    }

                }
            }
        } else {
            managerList = new ArrayList<>();
        }

        return managerList;
    }

    /**
     * 기존에 등록된 사업자 등록번호 있는지 체크
     *
     * @param businessNumber
     */
    public void checkBusinessNumber(String businessNumber) {
        if (ObjectUtils.isEmpty(businessNumber)) {
            throw new CustomException(CustomError.PARTNER_JOIN_BUSINESS_NUMBER_ERROR);
        }
        // 사업자 번호 숫자 검사
        if (!isNumberic(businessNumber)) {
            throw new CustomException(CustomError.PARTNER_JOIN_BUSINESS_NUMBER_ERROR);
        }
    }

    /**
     * 아이디 유효성 검사
     *
     * @param id
     */
    public void checkId(String id) {
        // 빈값 체크
        if (ObjectUtils.isEmpty(id)) {
            throw new CustomException(CustomError.PARTNER_JOIN_ID_ERROR);
        }
        // 영문 소문자 및 숫자 조합
        if (!id.matches("^(?=.*[a-z])(?=.*\\d)[a-z\\d]+$")) {
            throw new CustomException(CustomError.PARTNER_ID_STRING_ERROR);
        }
        // 아이디 최소 수
        if (id.length() < ID_LENGTH_MIN) {
            throw new CustomException(CustomError.PARTNER_ID_LENGTH_MIN);
        }
        // 아이디 최대 수
        if (id.length() > ID_LENGTH_MAX) {
            throw new CustomException(CustomError.PARTNER_ID_LENGTH_MAX);
        }
        // 중복 아이디
        if (checkDupleId(id)) {
            throw new CustomException(CustomError.PARTNER_ID_DUPLE);
        }
    }

    /*****************************************************
     *  SubFunction - SELECT
     ****************************************************/
    /**
     * 반려 사유 조회
     *
     * @param idx
     * @return
     */
    private String getPartnerRejectReason(Integer idx) {
        return partnerDaoSub.getPartnerRejectReason(idx);
    }

    /**
     * 중복아이디 검색
     *
     * @param id
     * @return 카운트 값
     */
    public Boolean checkDupleId(String id) {
        Integer iCount = partnerDaoSub.getCountById(id);

        // 0 이상이면 true , 아니면 false
        return iCount > 0;
    }

    /*****************************************************
     *  SubFunction
     ****************************************************/

    /**
     * 숫자 판별
     *
     * @param str
     * @return
     */
    private static boolean isNumberic(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    private void stateText(List<PartnerDto> list) {
        for (PartnerDto dto : list) {
            stateText(dto);
        }
    }

    /**
     * 텍스트 변환
     *
     * @param dto
     */
    private void stateText(PartnerDto dto) {

        if (!ObjectUtils.isEmpty(dto.getState())) {
            if (dto.getState() == PARTNER_STATE_LEAVE) {
                dto.setStateText(super.langMessage("lang.partner.state0")); // 탈퇴
                dto.setStateBg("bg-danger");
            } else if (dto.getState() == PARTNER_STATE_NORMAL) {
                dto.setStateText(super.langMessage("lang.partner.state.1")); // 정상
                dto.setStateBg("bg-success");
            } else if (dto.getState() == PARTNER_STATE_ADDITIONAL_INFO) {
                dto.setStateText(super.langMessage("lang.partner.state.2")); // 추가정보입력 상태 (회원 가입 중)
                dto.setStateBg("bg-info");
            } else if (dto.getState() == PARTNER_STATE_READY) {
                dto.setStateText(super.langMessage("lang.partner.state.3")); // 대기 중
                dto.setStateBg("bg-info");
            } else if (dto.getState() == PARTNER_STATE_JUDGE) {
                dto.setStateText(super.langMessage("lang.partner.state.4")); // 심사 중
                dto.setStateBg("bg-primary");
            } else if (dto.getState() == PARTNER_STATE_JUDGE_BACK) {
                dto.setStateText(super.langMessage("lang.partner.state.5")); // 반려 (확인 요청)
                dto.setStateBg("bg-warning");
            } else if (dto.getState() == PARTNER_STATE_RETRIEVE) {
                dto.setStateText(super.langMessage("lang.partner.state.6")); // 회수
                dto.setStateBg("bg-info");
            }
        }

        if (dto.getState() == 9) {
            PartnerDto restrainInfo = partnerRestrainDaoSub.getRestrainInfo(dto.getIdx());

            try {
                // 현재 시간 UTC 가져오기
                String nowDate = dateLibrary.getDatetime();
                // 로컬 시간으로 변경
                nowDate = dateLibrary.utcToLocalTime(nowDate);

                // 날짜 변환 string -> date
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                dateformat.setLenient(false);

                Date startDate = dateformat.parse(restrainInfo.getStartDate());
                Date endDate = dateformat.parse(restrainInfo.getEndDate());
                Date thisDate = dateformat.parse(nowDate);

                // 제재 시작시간 전이면
                if (thisDate.before(startDate)) {
                    dto.setStateText(super.langMessage("lang.restrain.partner.wait")); // 제재 대기
                    dto.setStateBg("badge-warning");
                } // 제재 기간 이면
                else if (startDate.equals(thisDate) || startDate.before(thisDate) && endDate.after(thisDate)) {
                    dto.setStateText(super.langMessage("lang.restrain.partner.ing")); // 제재중
                    dto.setStateBg("badge-danger");
                } else {
                    dto.setStateText(super.langMessage("lang.partner.state.1")); // 정상
                    dto.setStateBg("bg-success");
                    dto.setState(1);
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
