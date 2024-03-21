package com.architecture.admin.models.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReportDto {
    //idx
    private Long idx;

    //채팅방 Uuid
    private String roomUuid;

    // 신고 한 사람
    private String memberUuid;

    //메세지 작성 한 사람
    private String targetMemberUuid;

    //내용
    private String message;

    // 작석일[마이크로세컨즈 포함 값]
    private String score;

    // 상태 값 0: 삭제 / 1 : 정상
    private Integer state;

    //작성일
    private String regDate;

    //작성일
    private String regDateTz;

    //신고자
    private String memberNick;

    //신고자 ID
    private String id;

    //신고 당한 자
    private String targetMemberNick;

    //신고 당한 자 ID
    private String targetId;

    // sql
    private Long insertedIdx;
    private Long affectedRow;
}
