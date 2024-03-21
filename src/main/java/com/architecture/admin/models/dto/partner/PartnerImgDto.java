package com.architecture.admin.models.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerImgDto {

    private Integer idx;            // idx
    private Integer partnerIdx;     // 파트너 idx
    private Integer type;           // 유형 (0:사업자등록증, 1:통신판매업사본, 2:인감증명서, 3:통장사본)
    private String path;            // 이미지 경로
    private String fileName;        // 이미지 파일명
    private String url;             // 이미지 url
    private Integer sort;           // 이미지 순서
    private Integer state;          // 상태값
    private String regDate;         // 등록일
    private String regDateTz;       // 등록일 타임존
}
