package com.architecture.admin.models.daosub.qna;

import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.qna.PartnerQnaDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PartnerQnaDaoSub {
    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 1:1문의 메뉴 목록
     *
     * @return
     */
    List<PartnerQnaDto> getMenuList();

    /**
     * 매크로 타이틀 리스트
     *
     * @return
     */
    List<PartnerQnaDto> getMacroTitleList();

    /**
     * 매크로 내용 조회
     *
     * @param macroIdx
     * @return
     */
    String getMacroContents(int macroIdx);
    /**
     * 1:1문의 목록
     *
     * @param searchDto
     * @return list
     */
    List<PartnerQnaDto> getListQna(SearchDto searchDto);

    /**
     * 전체 카운트
     *
     * @param searchDto
     * @return count
     */
    int getTotalCount(SearchDto searchDto);

    /**
     * 1:1문의 상세 정보
     *
     * @param idx
     * @return
     */
    PartnerQnaDto getViewQna(int idx);
}
