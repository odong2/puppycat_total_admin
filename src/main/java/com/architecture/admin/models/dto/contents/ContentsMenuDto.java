package com.architecture.admin.models.dto.contents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentsMenuDto {
    /**
     * sns_contents_menu
     */
    private Long idx; // 일련번호
    private String name; // 메뉴이름
    private Integer state; // 상태값
    private String regDate; // 등록일
    private String regDateTz; // 등록일 타임존

}
