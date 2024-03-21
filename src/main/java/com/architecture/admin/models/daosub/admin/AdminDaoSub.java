package com.architecture.admin.models.daosub.admin;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminDaoSub {

    /**
     * id 에 해당하는 카운트 가져오기: 신규 입력 시 중복되는 id가 있는지 체크에 사용
     *
     * @param adminDto
     * @return
     */
    Integer getCountById(AdminDto adminDto);


    /**
     * 로그인 성공시 회원 정보 가져오기
     *
     * @param adminDto ( id / password )
     * @return AdminDto
     */
    AdminDto getInfoForLogin(AdminDto adminDto);

    /**
     * 관리자 목록
     *
     * @param searchDto
     * @return
     */
    List<AdminDto> getListAdmin(SearchDto searchDto);

    /**
     * 관리자 소셜 계정 목록
     *
     * @return
     */
    List<MemberDto> getListMemberAdmin();

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 관리자 상세
     *
     * @param idx ( sns_admin.idx)
     * @return
     */
    AdminDto getViewAdmin(@Param("idx") int idx);

    /**
     * 관리자 레벨 가져오기
     *
     * @param id (total_admin.id)
     * @return
     */
    Integer getAdminLevel(String id);

    int getAuthCountById(AdminDto adminDto);

    /**
     * 관리자 idx 조회
     *
     * @param id
     * @return
     */
    Integer getAdminIdxById(String id);
}
