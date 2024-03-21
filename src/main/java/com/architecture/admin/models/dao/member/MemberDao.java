package com.architecture.admin.models.dao.member;

import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberDao {

    /*****************************************************
     * Insert
     ****************************************************/
    /**
     * 닉네임 로그 인서트
     *
     * @param memberDto  nick idx gender
     * @return insertedId
     */
    int insertNickLog(MemberDto memberDto);

    /*****************************************************
     * Update
     ****************************************************/
    /**
     * 닉네임 수정
     *
     * @param memberDto nick idx gender idx
     * @return affectedRow
     */
    int updateNick(MemberDto memberDto);

    /**
     * 회원 정보 수정
     *
     * @param memberDto lnag
     * @return affectedRow
     */
    int updateInfo(MemberDto memberDto);
}
