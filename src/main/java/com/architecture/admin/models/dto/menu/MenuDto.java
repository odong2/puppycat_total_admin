package com.architecture.admin.models.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/*****************************************************
 * SNS 회원
 ****************************************************/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDto {
    // base attribute
    private Integer idx;
    private Integer menuIdx;
    private Integer parent;
    private Integer sort;
    private Integer level;
    private Integer state;

    private String name;
    private String link;
    private String lang;
    private String regDate;

    // sql
    private Integer insertedIdx;
    private Integer affectedRow;
    private Integer menuInsertedName;
    private Integer menuAffectedRow;

}