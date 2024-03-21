package com.architecture.admin.models.dto.contents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentsDto {

    /**
     * sns_contents
     */
    private Long idx; // 일련번호
    private String uuid; //컨텐츠 고유 값
    private Long memberIdx; //member idx
    private String memberUuid; // memberUuid
    private String memberId; // memberId
    private String memberNick; // memberNick
    private Long menuIdx; // sns_contents_menu.idx
    private String menuName; // sns_contents_menu.name
    private String contents; // 내용
    private String thumbnailUrl; // text
    private Integer imageCnt; // 이미지 수
    private Integer isComment; // 댓글상태 0:비활성화 / 1:활성화
    private Integer isLike; // 좋아요상태 0:비활성화 / 1:활성화
    private Integer isView; // 공개여부 1:전체 / 2:팔로우만
    private Integer isKeep; // 보관상태 0:해제 / 1:보관
    private Long likeCnt; // 좋아요 카운트
    private Long saveCnt; // 저장 카운트
    private Long commentCnt; // 댓글 카운트
    private Long reportCnt;  // 신고 카운트
    private String modiDate; // 수정일
    private String modiDateTz; // 수정일 타임존
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존
    private Integer contentsState; //상태
    private String stateBg; //상태버튼 색상
    private String stateText; //상태 값

    // sql
    private Long insertedIdx;  // insert idx
    private Integer affectedRow; // 처리 row수

    private List<MultipartFile> uploadFile;    // 업로드 이미지
    private List<JSONObject> imgTagList;     // 이미지 내 태그된 회원 List
    private Long imgIdx; // sns_contents_img.idx

    /**
     * sns_hash_tag
     */
    private Long hashTagIdx; // sns_hash_tag.idx
    private String hashTag; // 해시태그

    /**
     * sns_contents_location
     */
    private Long locationIdx; // sns_contents_location.idx
    private String location; // 위치정보

    /**
     * sns_contents_img
     */
    private Integer imgWidth;   // 이미지 원본 가로사이즈
    private Integer imgHeight;  // 이미지 원본 세로사이즈

    /**
     * sns_member_out
     */
    private String memberOutNick; // 탈퇴 회원 닉네임

    // etc
    private Integer imgCnt;       // 이미지 개수
    private String viewType;   // 상세 타입
    private String reportContents;   // 신고 당시 내용

}
