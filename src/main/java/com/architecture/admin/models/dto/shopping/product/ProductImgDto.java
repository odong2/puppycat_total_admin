package com.architecture.admin.models.dto.shopping.product;

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
public class ProductImgDto {

    private Integer idx;        // 일련 번호
    private Integer productIdx;
    private String url;
    private String uploadName;
    private String uploadPath;
    private Integer imgWidth;
    private Integer imgHeight;
    private Integer type;
    private Integer sort;
    private Integer state;
    private String modiDate;
    private String regDate;

    // sql
    private Long insertedIdx;

}
