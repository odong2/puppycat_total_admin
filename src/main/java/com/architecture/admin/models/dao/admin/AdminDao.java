package com.architecture.admin.models.dao.admin;

import com.architecture.admin.models.dto.admin.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface AdminDao {

    /*****************************************************
     * Insert
     ****************************************************/
    /**
     * 관리자 회원가입
     *
     * @param adminDto ( id / password / regdate)
     * @return
     */
    Integer insert(AdminDto adminDto);

    /*****************************************************
     * Update
     ****************************************************/
    /**
     * admin 마지막 로그인 날짜 업데이트
     *
     * @param adminDto ( lastLoginDate / idx )
     */
    void updateLastDate(AdminDto adminDto);

    /**
     * 관리자 수정
     *
     * @param adminDto (state / level/ idx)
     * @return
     */
    int modifyAdmin(AdminDto adminDto);

    /**
     * 내 정보 수정
     *
     * @param adminDto
     * @return
     */
    int modifyMyPage(AdminDto adminDto);

    /**
     * 비밀번호 변경
     *
     * @param adminDto
     * @return
     */
    int modifyPassword(AdminDto adminDto);

}
