package com.architecture.admin.models.dto.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerQnaDto {

    private Integer idx;            // 고유번호
    private Integer adminIdx;       // total_admin.idx
    private Integer menuIdx;        // 메뉴번호
    private Integer partnerIdx;     // shop_partner.idx
    private Integer partnerMainIdx; // shop_partner.main_idx
    private Integer questionIdx;    // shop_partner_question.idx
    private Integer answerIdx;      // shop_partner_question_answer.idx
    private Integer answerState;    // 답변 여부
    private String answerStateText; // 답변 여부 문자변환
    private String answerStateBg;   // 답변 여부 bg 색상
    private Integer state;          // 답변 여부 상태값
    private Integer show;           // 상태값
    private String stateText;       // 상태값 문자변환
    private String stateBg;         // 상태 bg 색상
    private String title;           // 제목
    private String contents;        // 내용
    private String answerContents;  // 답변 내용
    private String name;            // 이름
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존
    private String answerRegDate;   // 답변 등록일
    private String modiDate;        // 수정일
    private String modiDateTz;      // 수정일 타임존
    private String menuName;        // 메뉴이름
    private String partnerId;       // shop_partner.id
    private String adminId;         // total_admin.id
    private String adminName;       // total_admin.name
    private String companyName;     // shop_partner_detail.company_name

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

}
