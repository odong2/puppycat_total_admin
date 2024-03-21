package com.architecture.admin.models.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long idx; // 일련번호
    private Long contentsIdx; // 컨텐츠 idx
    private Long parentIdx; // 부모 idx
    private String memberUuid;
    private String memberId; // member id
    private String memberNick; // member nick
    private Long memberIdx; // member idx
    private String contents; //컨텐츠 내용
    private Integer state; // 상태
    private String modiDate; // 수정일
    private String modiDateTz; // 수정일 타임존
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존
    private String stateBg; // 상태버튼 색상
    private String stateText; // 상태 값
    private String division; // 댓글/답글 구분값

    /**
     * sns_contents
     */
    private String contentsMemberNick;   // 컨텐츠 작성자 닉네임
    private String contentsMemberIdx;    // 컨텐츠 작성자 아이디
    private String contentsThumbnailUrl; // 컨텐츠 이미지 url
    private String contentsRegDate;      // 컨텐츠 등록일
    private Integer contentsState;       // 컨텐츠 상태
    private String contentsStateBg;      // 컨텐츠 상태 색상
    private String contentsStateText;    // 컨텐츠 상태 값
    private Long likeCnt; // 좋아요 카운트
    private Long reportCnt;  // 신고 카운트
    private Long commentCnt; // 댓글 카운트

    /**
     * sns_member_out
     */
    private String memberOutNick;        // 탈퇴 회원 닉네임

    // etc
    private String viewType;   // 상세 타입 (2: 신고)
    private String reportContents;   // 신고 당시 내용

}
