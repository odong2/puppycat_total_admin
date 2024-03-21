package com.architecture.admin.models.commondao.faq;

import com.architecture.admin.models.dto.faq.FaqDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FaqDao {

    /*****************************************************
     * Insert
     ****************************************************/
    /**
     * 자주하는 질문 등록
     *
     * @param faqDto menuIdx title adminId regdate
     */
    void insertFaq(FaqDto faqDto);

    /**
     * 자주하는 질문 제목 등록
     *
     * @param faqDto noticeIdx title regdate
     */
    void insertFaqTitle(FaqDto faqDto);

    /**
     * 자주하는 질문 내용 등록
     *
     * @param faqDto noticeIdx contents regdate
     */
    void insertFaqContents(FaqDto faqDto);

    /*****************************************************
     * Update
     ****************************************************/
    /**
     * 자주하는 질문 수정
     *
     * @param faqDto idx menuIdx title adminId
     * @return affectedRow
     */
    Integer updateFaq(FaqDto faqDto);

    /**
     * 자주하는 질문 제목 수정
     *
     * @param faqDto noticeIdx title regdate
     */
    void updateFaqTitle(FaqDto faqDto);

    /**
     * 자주하는 질문 내용 수정
     *
     * @param faqDto noticeIdx contents regdate
     */
    void updateFaqContents(FaqDto faqDto);
}
