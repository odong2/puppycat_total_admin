package com.architecture.admin.models.dto.log;

import lombok.Data;

/*****************************************************
 * member access log
 ****************************************************/
@Data
public class MemberAccessLogDto {
    private Long idx;        // 고유번호
    private String memberUuid; // 본인 memberUuid
    private String id;        // ID
    private String nick;        // 닉네임
    private String appKey;        // 앱 키
    private String domain;        // 도메인
    private String appVer;        // 앱 버전
    private String accessIp;        // 아이피
    private String iso;        // 사용 지역
    private String regDate;        // 등록일
    private String regDateTz;      // 등록일 타임존
}
