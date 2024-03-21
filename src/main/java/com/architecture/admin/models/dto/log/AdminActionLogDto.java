package com.architecture.admin.models.dto.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/*****************************************************
 * admin action log
 ****************************************************/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminActionLogDto {

    private Integer idx;    // PRIMARY KEY
    private String id;      // 관리자 아이디
    private String referrer;// referrer
    private String sClass;  // action class
    private String method;  // action method
    private String params;  // action params: 전달된 인자값
    private String ip;      // ip
    private String regDate; // 등록일

    private Integer insertedId; // 입력된 idx
    private Integer affectedRow;// 처리 row수
}
