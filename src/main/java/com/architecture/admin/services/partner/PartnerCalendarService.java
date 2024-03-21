package com.architecture.admin.services.partner;

import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.partner.PartnerCalendarDaoSub;
import com.architecture.admin.models.dto.partner.PartnerHolidayDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerCalendarService extends BaseService {

    private final PartnerCalendarDaoSub calendarDaoSub;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /*****************************************************
     *  Module
     ****************************************************/

    /**
     * 국가 공휴일 리스트
     *
     * @return : 국가 공휴일 리스트
     */
    @SneakyThrows
    public List<PartnerHolidayDto> getHolidayList(String searchYear) {

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); //날짜 포멧

        List<PartnerHolidayDto> publicHolidayList = calendarDaoSub.getHolidayList(searchYear);
        // 국가 공휴일 리스트

        if (!ObjectUtils.isEmpty(publicHolidayList)) {
            // 종료 날짜 하루 플러스 -> fullcalendar 종료 날짜가 하루 플러스 해서 계산하기 때문
            for (PartnerHolidayDto publicHolidayDto : publicHolidayList) {
                String endDate = publicHolidayDto.getEndDate();
                Date date = simpleDate.parse(endDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, +1);

                endDate = simpleDate.format(cal.getTime());
                publicHolidayDto.setEndDate(endDate);
            }
        }

        return publicHolidayList;
    }

    /**
     * 국가 공휴일 정보 조회
     *
     * @param idx : 국가 공휴일 idx
     * @return
     */
    public PartnerHolidayDto getHolidayInfoByIdx(Integer idx) {

        // 공휴일 유무 조회
        int result = calendarDaoSub.getHolidayCntByIdx(idx);

        if (result < 1) {
            throw new CustomException(CustomError.PARTNER_HOLIDAY_NOT_EXIST); // 존재하지 않는 공휴일입니다.
        }

        return calendarDaoSub.getHolidayInfoByIdx(idx);
    }

    /*****************************************************
     *  SubFunction - SELECT
     ****************************************************/

    /**
     * 휴무일 중복 체크
     *
     * @param holidayDto : searchConditionList, partnerIdx
     */
    private void checkHolidayDupl(PartnerHolidayDto holidayDto) {
        // 해당 기간에 등록된 일정 유무 조회
        int cnt = calendarDaoSub.getHolidayCnt(holidayDto);

        if (cnt > 0) {
            throw new CustomException(CustomError.PARTNER_HOLIDAY_DUPL_ERROR); // 이미 해당 기간에 등록된 휴무일이 있습니다.
        }
    }


    /*****************************************************
     *  SubFunction
     ****************************************************/

    /**
     * 날짜 검색조건 생성
     *
     * @return : 검색 날짜 리스트
     */
    @SneakyThrows
    private List<PartnerHolidayDto> createSearchDateCondition(PartnerHolidayDto holidayDto) {

        String startDate = holidayDto.getStartDate();
        String endDate = holidayDto.getEndDate();
        // 날짜 형식 변환
        LocalDate startLocalDate = LocalDate.parse(startDate.trim(), dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate.trim(), dateTimeFormatter);

        // 검색 조건 리스트
        List<PartnerHolidayDto> searchDateList = new ArrayList<>();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); //날짜 포멧

        PartnerHolidayDto searchDate = new PartnerHolidayDto();
        searchDate.setStartDate(startDate);
        searchDate.setEndDate(startDate);
        searchDateList.add(searchDate); // 검색 리스트 추가

        if (!startLocalDate.equals(endLocalDate)) {

            while (!startLocalDate.equals(endLocalDate)) {

                Date date = simpleDate.parse(String.valueOf(startLocalDate));

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, +1); // 하루 플러스

                String startDateStr = simpleDate.format(cal.getTime());

                PartnerHolidayDto searchDate2 = new PartnerHolidayDto();

                searchDate2.setStartDate(startDateStr);
                searchDate2.setEndDate(startDateStr);

                searchDateList.add(searchDate2); // 검색 조건 리스트 추가

                // 하루 플러스한 날짜로 초기화
                startLocalDate = LocalDate.parse(startDateStr, dateTimeFormatter);
            }
        }

        return searchDateList;
    }

    /*****************************************************
     *  Validation
     ****************************************************/

    /**
     * 휴무일 등록 페이지 요청전 날짜 검사
     *
     * @param holidayDto : startDate, endDate
     */
    public void dateValidate(PartnerHolidayDto holidayDto) {

        String startDate = holidayDto.getStartDate();

        if (!ObjectUtils.isEmpty(startDate)) {
            LocalDate nowDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 날짜 형식 변환
            LocalDate startLocalDate = LocalDate.parse(startDate.trim(), dateTimeFormatter);

            // 오늘날짜 이전이면
            if (startLocalDate.isBefore(nowDate)) {
                throw new CustomException(CustomError.PARTNER_HOLIDAY_BEFORE_NOW_DATE_ERROR); // 당일 이전 휴무일 등록은 불가능합니다.
            }

            // 오늘날짜와 같으면
            if (startLocalDate.equals(nowDate)) {
                throw new CustomException(CustomError.PARTNER_HOLIDAY_NOW_DATE_ERROR); // 당일 휴무일 등록은 불가능합니다.
            }
        }

        if (!ObjectUtils.isEmpty(holidayDto.getStartDate())) {
            List<PartnerHolidayDto> searchConditionList = createSearchDateCondition(holidayDto);
            holidayDto.setSearchConditionList(searchConditionList);
            // 휴무일 중복 체크
            checkHolidayDupl(holidayDto);
        }
    }

}
