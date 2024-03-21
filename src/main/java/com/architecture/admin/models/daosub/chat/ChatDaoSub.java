package com.architecture.admin.models.daosub.chat;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.chat.ChatMessageReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ChatDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 채팅 신고 리스트
     *
     * @param searchDto
     * @return list
     */
    List<ChatMessageReportDto> getChatReportList(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getChatReportTotalCount(SearchDto searchDto);

    /**
     * 채팅 신고 View Info
     *
     * @param Integer
     */
    List<ChatMessageReportDto> getChatReportView(Integer idx);

}
