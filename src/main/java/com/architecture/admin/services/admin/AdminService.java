package com.architecture.admin.services.admin;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.admin.AdminDao;
import com.architecture.admin.models.daosub.admin.AdminDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService extends BaseService {

    private final AdminDao adminDao;

    private final AdminDaoSub adminDaoSub;


    /**
     * 관리자 목록
     *
     * @param searchDto
     * @return
     */
    public List<AdminDto> getListAdmin(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = adminDaoSub.getTotalCount(searchDto);
        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);
        // list
        List<AdminDto> listAdmin = adminDaoSub.getListAdmin(searchDto);

        if (!listAdmin.isEmpty()) {
            // 문자 변환
            stateText(listAdmin);
        }
        return listAdmin;
    }

    /**
     * 관리자 소셜 계정 목록
     *
     * @return 소셜 계정 목록
     */
    public List<MemberDto> getListMemberAdmin() {
        return adminDaoSub.getListMemberAdmin();
    }

    /**
     * 관리자 상세
     *
     * @param idx (sns_admin.idx)
     * @return 관리자 상세
     */
    public AdminDto getViewAdmin(Integer idx) {

        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.IDX_ERROR);
        }

        AdminDto viewAdmin = adminDaoSub.getViewAdmin(idx);

        if (viewAdmin != null) {
            // 문자 변환
            stateText(viewAdmin);
        }
        return viewAdmin;
    }

    /**
     * 비밀번호 수정
     *
     * @param adminDto
     * @return
     */
    @SneakyThrows
    @Transactional
    public int modifyAdminPassword(AdminDto adminDto) {

        // 유효성 체크
        adminPasswordValidation(adminDto);

        // 변경할 비밀번호 암호화
        adminDto.setPassword(super.encrypt(adminDto.getNewPassword()));

        // 비밀번호 변경
        return adminDao.modifyPassword(adminDto);
    }

    /**
     * 내 정보 수정
     *
     * @param adminDto
     * @return
     */
    @Transactional
    public int modifyAdminMyPage(AdminDto adminDto) {

        // idx
        if (adminDto.getIdx() == null || adminDto.getIdx() < 1L) {
            throw new CustomException(CustomError.IDX_ADMIN_ERROR);
        }

        if (super.getAdminIdx("idx") != adminDto.getIdx()) {
            throw new CustomException(CustomError.MY_MODIFY_ERROR);
        }

        // 수정 전 내 정보 조회
        AdminDto beforeInfo = adminDaoSub.getViewAdmin(adminDto.getIdx());

        if (beforeInfo != null) {
            // level를 변경하는 경우 -> 수정 가능한 레벨인지 체크
            if (adminDto.getLevel() != null && beforeInfo.getLevel() != adminDto.getLevel()) {

                // 관리자 권한 체크
                super.checkAdminAccessLevel();

                // state를 변경하는 경우 -> 수정 가능한 레벨인지 체크
            } else if (adminDto.getState() != null && beforeInfo.getState() != adminDto.getState()) {

                // 관리자 권한 체크
                super.checkAdminAccessLevel();
            }
        }

        // 내 정보 수정
        return adminDao.modifyMyPage(adminDto);
    }

    /**
     * 관리자 수정
     *
     * @param adminDto (state / level/ idx)
     * @return
     * @throws Exception
     */
    public int modifyAdmin(AdminDto adminDto) {

        // idx
        if (adminDto.getIdx() == null || adminDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_MEMBER_ERROR);
        }

        return adminDao.modifyAdmin(adminDto);
    }

    /**
     * 관리자 idx 조회
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Integer getAdminIdxById(String id) {

        if (ObjectUtils.isEmpty(id)) {
            throw new CustomException(CustomError.IDX_ERROR); // 관리자 연동 오류
        }

        Integer idx = adminDaoSub.getAdminIdxById(id);

        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.IDX_ERROR); // 관리자 연동 오류
        }

        return idx;
    }


    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<AdminDto> list) {
        for (AdminDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto AdminDto
     */
    public void stateText(AdminDto dto) {
        if (dto.getState() != null) {
            if (dto.getState() == 0) {
                // 탈퇴
                dto.setStateText(super.langMessage("lang.admin.state.out"));
                dto.setStateBg("badge-danger");
            } else if (dto.getState() == 1) {
                // 정상
                dto.setStateText(super.langMessage("lang.admin.state.normal"));
                dto.setStateBg("badge-success");
            } else if (dto.getState() == 2) {
                // 대기
                dto.setStateText(super.langMessage("lang.admin.state.wait"));
                dto.setStateBg("badge-warning");
            } else if (dto.getState() == 3) {
                // 정지
                dto.setStateText(super.langMessage("lang.admin.state.block"));
                dto.setStateBg("badge-danger");
            }
        }
    }


    /*****************************************************
     *  Validation
     ****************************************************/
    /**
     * 비밀번호 정보 유효성 검사
     *
     * @param adminDto
     */
    private void adminPasswordValidation(AdminDto adminDto) throws Exception {
        if (super.getAdminIdx("idx") != adminDto.getIdx()) {
            throw new CustomException(CustomError.MY_MODIFY_ERROR);
        }
        /** 기본 유효성 검사 **/
        // 관리자 idx
        if (adminDto.getIdx() == null || adminDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_ADMIN_ERROR); // // 관리자 idx가 없습니다.
        }

        // 이전 비밀번호
        if (adminDto.getOldPassword() == null || adminDto.getOldPassword().isEmpty()) {
            throw new CustomException(CustomError.OLD_PASSWORD_EMPTY); // 이전 비밀번호를 입력해주세요.
        }

        // 변경할 비밀번호
        if (adminDto.getNewPassword() == null || adminDto.getNewPassword().isEmpty()) {
            throw new CustomException(CustomError.NEW_PASSWORD_EMPTY); // 변경할 비밀번호를 입력해주세요.
        }

        // 변경할 비밀번호 확인란
        if (adminDto.getPasswordConfirm() == null || adminDto.getPasswordConfirm().isEmpty()) {
            throw new CustomException(CustomError.PASSWORD_CONFIRM_EMPTY); // 변경할 비밀번호 확인란을 입력해주세요.
        }

        // 이전 비밀번호와 변경할 비밀번호가 같은 경우
        if (adminDto.getOldPassword().equals(adminDto.getNewPassword())) {
            throw new CustomException(CustomError.PASSWORD_CORRESPOND); // 변경할 비밀번호가 이전 비밀번호와 같습니다.
        }

        // 이전 비밀번호와 변경할 비밀번호 확인란이 같은 경우
        if (adminDto.getOldPassword().equals(adminDto.getPasswordConfirm())) {
            throw new CustomException(CustomError.PASSWORD_CORRESPOND); // 변경할 비밀번호가 이전 비밀번호와 같습니다.
        }

        // 변경할 비밀번호와 비밀번호 확인란이 다른 경우
        if (!adminDto.getNewPassword().equals(adminDto.getPasswordConfirm())) {
            throw new CustomException(CustomError.PASSWORD_NOT_CORRESPOND); // 변경할 비밀번호와 비밀번호 확인란이 일치하지 않습니다.
        }

        /** DB 조회 후 검사 **/
        // 변경하기 전 관리자 정보 조회
        AdminDto admin = adminDaoSub.getViewAdmin(adminDto.getIdx());

        // 관리자의 현재 비밀번호(암호화 상태)
        String nowPassword = admin.getPassword();

        // 입력받은 이전 비밀번호 값 암호화 처리
        String oldPasswordInput = super.encrypt(adminDto.getOldPassword());

        // 입력받은 변경할 비밀번호 값 암호화 처리
        String newPasswordInput = super.encrypt(adminDto.getNewPassword());

        // 입력받은 변경할 비밀번호 확인란 값 암호화 처리
        String passwordConfirmInput = super.encrypt(adminDto.getPasswordConfirm());

        // 입력받은 현재 비밀번호와 실제 현재 비밀번호가 다를 경우
        if (!nowPassword.equals(oldPasswordInput)) {
            throw new CustomException(CustomError.OLD_PASSWORD_ERROR); // 이전 비밀번호가 올바르지 않습니다.
        }

        // 변경할 비밀번호가 현재 비밀번호와 같을 경우
        if (nowPassword.equals(newPasswordInput)) {
            throw new CustomException(CustomError.PASSWORD_CORRESPOND); // 변경할 비밀번호가 현재 비밀번호와 같습니다.
        }

        // 변경할 비밀번호 확인란이 현재 비밀번호와 같을 경우
        if (nowPassword.equals(passwordConfirmInput)) {
            throw new CustomException(CustomError.PASSWORD_CORRESPOND); // 변경할 비밀번호가 현재 비밀번호와 같습니다.
        }
    }

}
