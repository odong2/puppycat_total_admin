package com.architecture.admin.models.daosub.contents;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.contents.ContentsImgDto;
import com.architecture.admin.models.dto.contents.ContentsImgTagDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContentsDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 회원 작성 게시글 최근 20개 리스트 by memberUuid
     *
     * @param contentsDto memberUuid
     * @return list
     */
    List<ContentsDto> getContentLastTwentyCasesList(ContentsDto contentsDto);

    /**
     * 컨텐츠 메뉴 리스트
     *
     * @param contentsDto
     * @return 메뉴 리스트
     */
    List<ContentsDto> getMenuList(ContentsDto contentsDto);

    /**
     * 전체 게시글 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 게시글 목록
     *
     * @param searchDto
     * @return list
     */
    List<ContentsDto> getList(SearchDto searchDto);

    /**
     * 컨텐츠 상세
     *
     * @param contentsDto 콘텐츠 idx
     * @return list
     */
    List<ContentsDto> getDetailList(ContentsDto contentsDto);

    /**
     * 컨텐츠 이미지 상세
     *
     * @param contentsDto 콘텐츠 idx
     * @return list
     */
    List<ContentsImgDto> getDetailImgList(ContentsDto contentsDto);

    /**
     * 이미지 태그 상세
     *
     * @param contentsImgDto 이미지 idx
     * @return list
     */
    List<ContentsImgTagDto> getImgTag(ContentsImgDto contentsImgDto);

    /**
     * 컨텐츠 리스트 조회
     *
     * @param idx
     * @return
     */
    ContentsDto getListByIdx(Long idx);

    /**
     * 댓글 좋아요 한 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getLikeMemberTotalCnt(SearchDto searchDto);

    /**
     * 댓글 좋아요 한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getLikeMemberList(SearchDto searchDto);

    /**
     * 댓글 저장한 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getSaveMemberTotalCnt(SearchDto searchDto);

    /**
     * 댓글 저장한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getSaveMemberList(SearchDto searchDto);

    /**
     * 댓글 작성한 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getCommentsTotalCnt(SearchDto searchDto);

    /**
     * 댓글 작성한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getCommentsList(SearchDto searchDto);

    /**
     * 급상승 인기 게시물 카운트
     *
     * @param searchDto
     * @return
     */
    Integer getHourPopularTotalCnt(SearchDto searchDto);

    /**
     * 급상승 인기 게시물 리스트
     *
     * @param searchDto
     * @return
     */
    List<ContentsDto> getHourPopularList(SearchDto searchDto);

    /**
     * 주간 인기 게시물 카운트
     *
     * @param searchDto
     * @return
     */
    Integer getWeekPopularTotalCnt(SearchDto searchDto);

    /**
     * 주간 인기 게시물 리스트
     *
     * @param searchDto
     * @return
     */
    List<ContentsDto> getWeekPopularList(SearchDto searchDto);

    /**
     * 컨텐츠 신고 카운트
     *
     * @param searchDto
     * @return
     */
    int getReportTotalCnt(SearchDto searchDto);

    /**
     * 신고 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getReportMemberList(SearchDto searchDto);
}
