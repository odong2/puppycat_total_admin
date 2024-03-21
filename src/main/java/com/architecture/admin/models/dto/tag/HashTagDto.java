package com.architecture.admin.models.dto.tag;

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
public class HashTagDto {
    /**
     * sns_hash_tag
     */
    private Long idx;           // 일련번호
    private Long hashTagCnt;    // 해시태그 사용 cnt
    private String hashTag;     // 해시태그

    /**
     * sns_contents_hash_tag_mapping
     */
    private Long contentsIdx;    // 컨텐츠idx
    private Long hashTagIdx;     // 해시태그 idx
    private Integer state;       // 상태값
    private String regDate;      // 등록일
    private String regDateTz;    // 등록일 타임존

    /**
     * sns_contents_comment_hash_tag_mapping
     */
    private Long commentIdx;    // 댓글idx

    // etc
    private String type;        // 컨텐츠인지 댓글인지
    private String contents;    // 컨텐츠,댓글 내용

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;

}
