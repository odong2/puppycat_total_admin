package com.architecture.admin.models.daosub.notice;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.NoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoticeDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 공지사항 목록
     *
     * @param searchDto
     * @return list
     */
    List<NoticeDto> getList(SearchDto searchDto);

    /**
     * 공지사항 메뉴 목록
     *
     * @param noticeDto
     * @return list
     */
    List<NoticeDto> getMenuList(NoticeDto noticeDto);

    /**
     * 공지사항 상세 정보
     *
     * @param idx sns_board_notice.idx
     * @return
     */
    NoticeDto getViewNotice(int idx);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

}
