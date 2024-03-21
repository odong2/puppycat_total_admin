package com.architecture.admin.models.dto.inspect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInspectDto {
    private Integer currentBuildNumber;
    private Integer minBuildNumber;
    private Integer recommendBuildNumber;
    private Integer majorNumber;
    private Integer minorNumber;
    private Integer patchNumber;
    private String updateDate;
    private String forceTitle;
    private String forceContents;
    private String forcePatchNote;
    private String title;
    private String contents;
    private String patchNote;

}
