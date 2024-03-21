package com.architecture.admin.models.daosub.comment;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.comment.CommentDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 댓글 목록
     *
     * @param searchDto
     * @return list
     */
    List<CommentDto> getList(SearchDto searchDto);

    /**
     * 최근 댓글 20개 리스트 by memberIdx
     *
     * @param commentDto memberIdx
     * @return list
     */
    List<CommentDto> getCommentLastTwentyCasesList(CommentDto commentDto);

    /**
     * 댓글 상세 내용
     *
     * @param commentDto idx, viewType
     * @return
     */
    CommentDto getDetailComment(CommentDto commentDto);

    /**
     * 좋아요한 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getLikeMemberTotalCnt(SearchDto searchDto);

    /**
     * 좋아요한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getLikeMemberList(SearchDto searchDto);

    /**
     * 신고한 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getReportMemberTotalCnt(SearchDto searchDto);

    /**
     * 신고한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getReportMemberList(SearchDto searchDto);

    /**
     * 댓글단 회원 카운트
     *
     * @param searchDto
     * @return
     */
    int getCommentsTotalCnt(SearchDto searchDto);

    /**
     * 댓글단 회원 리스트
     *
     * @param searchDto
     * @return
     */
    List<MemberDto> getCommentsList(SearchDto searchDto);

    /**
     * 댓글 신고 개수 조회
     *
     * @param idxList
     * @return
     */
    List<CommentDto> getReportCntByIdxList(List<Long> idxList);
}
