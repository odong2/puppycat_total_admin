package com.architecture.admin.models.daosub.follow;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.follow.FollowDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FollowDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 팔로우 목록
     *
     * @param searchDto
     * @return list
     */
    List<FollowDto> getList(SearchDto searchDto);

    /**
     * 회원 팔로잉 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    List<FollowDto> getFollowingList(FollowDto followDto);

    /**
     * 회원 팔로워 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    List<FollowDto> getFollowerList(FollowDto followDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 팔로워 카운트 가져오기 [ member_follow_cnt ]
     *
     * @param memberIdx 조회할 회원 idx
     * @return count
     */
    Long getTotalFollowerCnt(Long memberIdx);

    /**
     * 회원 팔로우/팔로워 전체 카운트
     *
     * @param searchDto
     * @return
     */
    int getMemberFollowTotalCount(SearchDto searchDto);

    /**
     * 회원 팔로우/팔로워 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getMemberFollowList(SearchDto searchDto);
}
