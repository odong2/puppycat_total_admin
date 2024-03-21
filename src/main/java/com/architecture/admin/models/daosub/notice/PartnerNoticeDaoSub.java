package com.architecture.admin.models.daosub.notice;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.PartnerNoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PartnerNoticeDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 공지사항 목록
     *
     * @param searchDto
     * @return list
     */
    List<PartnerNoticeDto> getList(SearchDto searchDto);

    /**
     * 공지사항 메뉴 목록
     *
     * @param partnerNoticeDto
     * @return list
     */
    List<PartnerNoticeDto> getMenuList(PartnerNoticeDto partnerNoticeDto);

    /**
     * 공지사항 상세 정보
     *
     * @param idx sns_board_notice.idx
     * @return
     */
    PartnerNoticeDto getViewNotice(int idx);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

}
