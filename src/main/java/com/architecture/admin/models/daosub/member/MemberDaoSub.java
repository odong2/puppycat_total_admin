package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MemberDaoSub {

    /**
     * 회원 목록
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getListMember(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 회원 상세
     *
     * @param memberIdx member.idx
     * @return
     */
    MemberDto getViewMember(Long memberIdx);

    /**
     * 최근 교류많은 유저 20명 리스트 by memberIdx
     *
     * @param memberDto memberIdx
     * @return list
     */
    List<MemberDto> getInteractiveLastTwentyCasesList(MemberDto memberDto);

    /**
     * 회원 닉네임 중복 확인
     *
     * @param memberDto
     * @return
     */
    int getCountByNick(MemberDto memberDto);

     /**
     * 회원 닉네임 가져오기 by uuid
     *
     * @param uuid 회원 uuid
     * @return 닉네임
     */
    String getMemberNickByUuid(String uuid);

    /**
     * 탈퇴 회원 닉네임 가져오기 by uuid
     *
     * @param uuid 탈퇴회원 uuid
     * @return 닉네임
     */
    String getOutMemberNickByUuid(String uuid);

    /**
     * 회원 카운트 값 by idx
     *
     * @param memberDto idx
     * @return count
     */
    int getCountByIdx(MemberDto memberDto);

    /**
     * 회원 idx 검색 by 닉네임
     *
     * @param nick 회원 닉네임
     * @return memberUuid
     */
    String getMemberUuidByNick(String nick);
    String getMemberUuidByIdx(Long memberIdx);

    /**
     * sns_member 정보 가져오기 by nick
     *
     * @param nick
     * @return
     */
    MemberDto getMemberByNick(String nick);

    /**
     * 회원 정보 조회
     *
     * @param uuidList
     * @return
     */
    List<MemberDto> getMemberInfoByUuidList(List<String> uuidList);

    /**
     * 회원 uuid 조회
     *
     * @param searchDto
     * @return
     */
    List<String> getUuidList(SearchDto searchDto);

    /**
     * 회원 정보 리스트 by MemberUuid
     *
     * @param memberUuidList
     * @return
     */
    List<MemberDto> getMemberInfoListByUuid(List<String> memberUuidList);

    /**
     * 팔로우/팔로워 랭킹 카운트
     *
     * @param searchDto
     * @return
     */
    int getRankTotalCount(SearchDto searchDto);

    /**
     * 팔로우/팔로워 랭킹 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getRankList(SearchDto searchDto);

    /**
     * 고유아이디에 해당하는 카운트 가져오기: 아이디 중복 체크에 사용
     *
     * @param uuid (고유아이디)
     * @return count
     */
    Integer getCountByUuid(String uuid);

    /**
     * 적용 카테고리 검색 카운트
     *
     * @param searchDto
     * @return
     */
    int getMemberSearchCount(SearchDto searchDto);

    /**
     * 적용 카테고리 검색 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getMemberSearchList(SearchDto searchDto);
}
