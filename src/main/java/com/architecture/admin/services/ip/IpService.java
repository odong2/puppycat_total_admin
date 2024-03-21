package com.architecture.admin.services.ip;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.ip.IpDao;
import com.architecture.admin.models.daosub.ip.IpDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.ip.IpDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class IpService extends BaseService {

    private final IpDao ipDao;
    private final IpDaoSub ipDaoSub;
    private final ExcelData excelData;
    private final AdminActionLogService adminActionLogService;// 관리자 action log 처리용

    /**
     * 관리자 허용 IP 리스트 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, Object> excelAdminIp(SearchDto searchDto) {

        // 검색어 공백제거
        if (!searchDto.getSearchWord().isEmpty()) {
            searchDto.setSearchWord(searchDto.getSearchWord().trim());
        }

        // send data set
        List<IpDto> adminIpList = null;

        // IP 개수 카운트
        int totalCnt = ipDaoSub.getAdminIpTotalCnt(searchDto);

        // IP가 있는 경우
        if (totalCnt > 0) {

            // IP 리스트 조회
            adminIpList = ipDaoSub.getAdminIpList(searchDto);

            // 상태값 & 관리자 구분값 문자변환
            convertText(adminIpList);
        }
        // 엑셀 데이터 생성
        return excelData.createExcelData(adminIpList, IpDto.class);
    }

    /**
     * 관리자 허용 IP 리스트 조회
     *
     * @param searchDto
     */
    @Transactional(readOnly = true)
    public JSONObject getAdminIpList(SearchDto searchDto) {

        // 검색어 공백제거
        if (!searchDto.getSearchWord().isEmpty()) {
            searchDto.setSearchWord(searchDto.getSearchWord().trim());
        }

        // 내보낼 데이터 set
        JSONObject joData = new JSONObject();
        List<IpDto> adminIpList = null;

        // IP 개수 카운트
        int totalCnt = ipDaoSub.getAdminIpTotalCnt(searchDto);

        // IP가 있는 경우
        if (totalCnt > 0) {

            // paging
            PaginationLibray pagination = new PaginationLibray(totalCnt, searchDto);
            searchDto.setPagination(pagination);

            // IP 리스트 조회
            adminIpList = ipDaoSub.getAdminIpList(searchDto);

            // 상태값 & 관리자 구분값 문자변환
            convertText(adminIpList);

            // paging 담기
            joData.put("params", new JSONObject(searchDto));
        }

        // list 담기
        joData.put("list", adminIpList);

        return joData;
    }

    /**
     * 관리자 허용 IP 상세
     *
     * @param idx
     */
    @Transactional(readOnly = true)
    public JSONObject getViewAdminIp(Long idx) {

        // IP idx 유효성 검사
        isAdminIpIdxValidate(idx);

        // IP 상세 조회
        IpDto viewAdminIp = ipDaoSub.getViewAdminIp(idx);

        if (viewAdminIp != null) {

            // 허용 ip 상세 상태값 & 관리자 구분값 문자변환
            convertText(viewAdminIp);
        }

        // 내보낼 데이터 set
        return new JSONObject(viewAdminIp);
    }

    /**
     * 관리자 허용 IP 등록
     *
     * @param ipDto
     */
    @Transactional
    public void insertAdminIp(IpDto ipDto) {

        // 등록할 IP 유효성 검사
        isInputDataValidate(ipDto);

        // IP 등록
        ipDto.setRegdate(dateLibrary.getDatetime()); // 등록일 set
        int result = ipDao.insertAdminIp(ipDto);

        // 등록 실패 시
        if (result < 1) {
            throw new CustomException(CustomError.ADMIN_IP_REGISTER_ERROR); // IP 등록을 실패하였습니다.
        }

        // 관리자 action log
        adminActionLogService.regist(ipDto, Thread.currentThread().getStackTrace());
    }

    /**
     * 관리자 허용 IP 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteAdminIp(Long idx) {

        // IP idx 유효성 검사
        isAdminIpIdxValidate(idx);

        // IP 삭제
        int result = ipDao.deleteAdminIp(idx);

        // 삭제 실패 시
        if (result < 1) {
            throw new CustomException(CustomError.ADMIN_IP_DELETE_ERROR); // IP 삭제를 실패하였습니다.
        }

        // 관리자 action log
        adminActionLogService.regist(idx, Thread.currentThread().getStackTrace());
    }

    /**
     * 관리자 허용 IP 수정
     *
     * @param ipDto
     */
    @Transactional
    public void updateAdminIp(IpDto ipDto) {

        // 수정할 데이터 유효성 검사
        isInputDataValidate(ipDto);

        // IP 수정
        ipDto.setRegdate(dateLibrary.getDatetime()); // 수정일 set
        int result = ipDao.updateAdminIp(ipDto);

        // 수정 실패 시
        if (result < 1) {
            throw new CustomException(CustomError.ADMIN_IP_MODIFY_ERROR); // IP 수정을 실패하였습니다.
        }

        // 관리자 action log
        adminActionLogService.regist(ipDto, Thread.currentThread().getStackTrace());
    }

    /*************************************************************
     * 문자변환
     *************************************************************/

    /**
     * 관리자 허용 IP
     * 문자변환 list
     */
    private void convertText(List<IpDto> list) {
        for (IpDto dto : list) {
            if (dto != null) {
                convertText(dto);
            }
        }
    }

    /**
     * 관리자 허용 IP
     * 문자변환 dto
     */
    private void convertText(IpDto dto) {

        if (dto.getState() != null) {

            // 미사용
            if (dto.getState() == 2) {
                dto.setStateText(super.langMessage("lang.ip.state.unuse"));
                dto.setStateBg("badge-danger");

                // 사용
            } else if (dto.getState() == 1) {
                dto.setStateText(super.langMessage("lang.ip.state.use"));
                dto.setStateBg("badge-success");
            }
        }
    }

    /*************************************************************
     * Validation
     *************************************************************/

    /**
     * 선택한 허용 IP idx 유효성 검사
     *
     * @param idx
     */
    private void isAdminIpIdxValidate(Long idx) {

        // 허용 IP idx가 없는 경우
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.ADMIN_IP_IDX_EMPTY); // 요청하신 IP 상세 정보를 찾을 수 없습니다.
        }
    }

    /**
     * 허용 IP 등록 & 수정
     * 입력 데이터 유효성 검사
     *
     * @param ipDto
     */
    private void isInputDataValidate(IpDto ipDto) {

        // IP 사용 상태
        if (ipDto.getState() == null || ipDto.getState() < 0 || ipDto.getState() > 2) {
            throw new CustomException(CustomError.ADMIN_IP_STATE_EMPTY); // IP 사용 상태를 선택해주세요.
        }

        // IP
        if (ipDto.getIp() == null || ipDto.getIp().isEmpty()) {
            throw new CustomException(CustomError.ADMIN_IP_EMPTY); // IP 주소를 입력해주세요.
        }

        // IP 설명
        if (ipDto.getMemo() == null || ipDto.getMemo().isEmpty()) {
            throw new CustomException(CustomError.ADMIN_IP_MEMO_EMPTY); // IP 주소 설명을 입력해주세요.
        }
    }
}
