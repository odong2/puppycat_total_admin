package com.architecture.admin.models.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {
    /**
     * 이미지 임시 저장(ckeditor5 사용)
     */
    private boolean uploaded;
    private String url;
}
