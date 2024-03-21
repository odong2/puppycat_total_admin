package com.architecture.admin.models.daosub.report;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.report.CommentReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentReportDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 댓글 신고 목록
     *
     * @param searchDto 검색조건, pagination
     * @return list
     */
    List<CommentReportDto> getList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto 검색조건
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 부모 댓글 내용 by commentIdx
     *
     * @param idx commentIdx
     * @return CommentReportDto
     */
    CommentReportDto getParentCommentByIdx(Long idx);

    /**
     * idx 검색
     *
     * @param idxList 검색조건
     * @return count
     */
    List<Long> getChkIdxList(List<Long> idxList);
}
