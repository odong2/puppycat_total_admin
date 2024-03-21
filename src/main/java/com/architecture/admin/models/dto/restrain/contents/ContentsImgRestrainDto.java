package com.architecture.admin.models.dto.restrain.contents;

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
public class ContentsImgRestrainDto {
    /**
     * sns_contents_img_restrain
     */
    private Long idx;         // idx
    private Long memberIdx;   // 회원 idx
    private String memberUuid; // 회원 uuid
    private Long imgIdx;      // 이미지 idx
    private String memo;      // 제재 사유
    private String adminId;   // 관리자 아이디
    private String regDate;   // 등록일
    private String regDateTz; // 등록일 타임존

    /**
     * 기타
     */
    private List<Long> idxList;  // 이미지 idx 리스트
    private Long contentsIdx;    // 컨텐츠 idx

    
}
