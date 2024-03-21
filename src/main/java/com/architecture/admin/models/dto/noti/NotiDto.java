package com.architecture.admin.models.dto.noti;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotiDto {

    //sns_notification_notice
    private long idx;           // 고유 번호
    private long memberIdx;     // 회원번호
    private Integer type;       // 알림 타입
    private String subType;     // 상세 타입
    private Long senderIdx;     // 알림 보내는 회원
    private long contentsIdx;   // 컨텐츠idx
    private long commentIdx;    // 댓글idx
    private String title;       // 제목
    private String body;        // 내용
    private String img;         // 이미지
    private String contents;    // 상세 내용
    private String adminId;     // 관리자ID
    private Integer state;      // 상태값
    private String delDate;     // 삭제일
    private String delDateTz;   // 삭제일 타임존
    private String regDate;     // 등록일
    private String regDateTz;   // 등록일 타임존

    // sns_member_notification_show
    private String showDate;     // 마지막 공지 확인일
    private String showDateTz;   // 마지막 공지 확인일 타임존

    private Long parentIdx;         // 부모댓글idx
    private String checkNotiDate;   // 공지 중복 체크할 데이트

    // sql
    private Long insertedIdx;
    private Integer affectedRow;
}
