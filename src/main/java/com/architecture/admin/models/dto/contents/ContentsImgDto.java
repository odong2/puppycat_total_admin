package com.architecture.admin.models.dto.contents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentsImgDto {
    /**
     * sns_contents_img
     */
    private Long idx; // 일련 번호
    private Long contentsIdx; // 컨텐츠 idx
    private String url; // url (도메인 제외)
    private Integer imgWidth;         // 가로 사이즈
    private Integer imgHeight;        // 세로 사이즈
    private String uploadPath;     // 이미지 경로
    private String uploadName;     // 이미지 파일명
    private Integer sort;          // 정렬순서
    private Integer state;         // 상태값
    private String regDate;        // 등록일
    private String regDateTz;      // 등록일 타임존
    private List<ContentsImgTagDto> imgTagList;

    // sql
    private Long insertedIdx;
    private Integer affectedRow;

    // 기타
    private List<Long> idxList;
    private String originUrl; // 이미지 원본 url
}
