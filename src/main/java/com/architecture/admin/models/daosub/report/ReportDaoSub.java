package com.architecture.admin.models.daosub.report;

import com.architecture.admin.models.dto.report.ReportCodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReportDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * 신고사유 리스트
     *
     * @return code 리스트 [sns_report_code]
     */
    List<ReportCodeDto> getListReportCode();

}
