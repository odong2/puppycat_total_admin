package com.architecture.admin.models.daosub.faq;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.faq.FaqDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FaqDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 자주하는 질문 목록
     *
     * @param searchDto
     * @return list
     */
    List<FaqDto> getFaqList(SearchDto searchDto);


    /**
     * 자주하는 질문 메뉴 목록
     *
     * @param faqDto
     * @return list
     */
    List<FaqDto> getMenuList(FaqDto faqDto);

    /**
     * 자주하는 질문 상세 정보
     *
     * @param idx sns_board_notice.idx
     * @return
     */
    FaqDto getViewFaq(int idx);
    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);
}
