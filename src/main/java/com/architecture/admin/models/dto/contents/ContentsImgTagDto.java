package com.architecture.admin.models.dto.contents;

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
public class ContentsImgTagDto {
    /**
     * sns_contents_img_tag
     */
    private Long idx; // 일련 번호
    private Long memberIdx; // 멤버 IDX
    private String memberUuId; // UUID
    private String memberNick; // 닉네임
    private Double width; // 좌우
    private Double height; // 상하
    private int tagState; // 상태 값
}
