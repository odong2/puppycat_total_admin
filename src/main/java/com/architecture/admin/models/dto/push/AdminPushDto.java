package com.architecture.admin.models.dto.push;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminPushDto {

    // sns_push_admin
    private Long idx;               // 고유 번호
    private Integer typeIdx;        // 푸시타입
    private String typeTitle;       // 푸시타입텍스트
    private String title;           // 제목
    private String body;            // 내용
    private Integer contentsType;   // 컨텐츠 타입
    private String moveLink;        // 이동 페이지
    private Long contentsIdx;       // 컨텐츠idx
    private String img;             // 이미지
    private String adminId;         // 관리자id
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존


    private List<MultipartFile> uploadFile;    // 프로필 업로드 이미지

    // sql
    private Long insertedIdx;
    private Long affectedRow;

}