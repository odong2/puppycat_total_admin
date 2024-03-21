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
public class ContentsLikeDto {
    /**
     * sns_contents_like
     */
    private Long idx;   // 일련번호
    private Long contentsIdx; // 콘텐츠 idx
    private Long memberIdx; // 회원 idx
    private Integer state; // 상태값
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존

    /**
     * sns_contents_like_cnt
     */
    private Long likeCnt;

    private String nick;
    private String intro;
    private String img;
}
