package com.architecture.admin.models.daosub.partner;

import com.architecture.admin.models.dto.partner.PartnerHolidayDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PartnerCalendarDaoSub {

    /**
     * 해당 기간에 등록된 국가 공휴일 개수
     *
     * @param holidayDto : partnerIdx, startDate, endDate
     * @return
     */
    int getHolidayCnt(PartnerHolidayDto holidayDto);


    /**
     * 국가 공휴일 리스트
     *
     * @param searchYear : 찾을 년도
     * @return
     */
    List<PartnerHolidayDto> getHolidayList(String searchYear);


    /**
     * 국가 공휴일 휴무일 정보 조회
     *
     * @param idx
     * @return
     */
    PartnerHolidayDto getHolidayInfoByIdx(Integer idx);

    /**
     * 공휴일 존재 유무
     * @param idx : 공휴일 idx
     * @return : cnt
     */
    int getHolidayCntByIdx(Integer idx);
}
