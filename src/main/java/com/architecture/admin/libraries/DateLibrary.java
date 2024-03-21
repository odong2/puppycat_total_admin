package com.architecture.admin.libraries;

import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.shopping.product.SearchProductDto;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/*****************************************************
 * 시간 라이브러리
 ****************************************************/
@Component
@Data
public class DateLibrary {

    /**
     * date 형식 시간 구하기
     *
     * @return UTC 기준 시간 yyyy-MM-dd hh:mm:ss
     */
    public String getDatetime() {
        java.util.Date dateNow = new java.util.Date(System.currentTimeMillis());

        // 타임존 UTC 기준
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatDatetime.setTimeZone(utcZone);

        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
        return formatDatetime.format(dateNow);
    }

    /**
     * 로컬시간을 UTC 시간으로 변경
     *
     * @param date 로컬 시간 yyyy-MM-dd hh:mm:ss
     * @return UTC 기준 시간 yyyy-MM-dd hh:mm:ss
     */
    public String localTimeToUtc(String date) {
        // 타임존 UTC 기준값
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatDatetime.setTimeZone(utcZone);
        Timestamp timestamp = Timestamp.valueOf(date);

        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
        return formatDatetime.format(timestamp);
    }

    /**
     * UTC 시간을 로컬시간으로 변경
     *
     * @param date UTC 시간 yyyy-MM-dd hh:mm:ss
     * @return 로컬 시간 yyyy-MM-dd hh:mm:ss
     */
     public String utcToLocalTime(String date) {
        // 입력시간을 Timestamp 변환
        long utcTime = Timestamp.valueOf(date).getTime();
        // 현재 로컬 타임 존을 가져옵니다.
        TimeZone localTimeZone = TimeZone.getDefault();
        // 로컬 타임 존의 썸머 타임 오프셋을 가져옵니다.
        int localOffset = localTimeZone.getOffset(utcTime);
        // UTC 시간에 로컬 오프셋을 더해 로컬 시간을 계산합니다.
        long localDateTime = utcTime + localOffset;
        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
        SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatDatetime.format(new Timestamp(localDateTime));
    }

    /**
     * timestamp 형식 시간 구하기
     */
    public String getTimestamp() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return String.valueOf(time.getTime() / 1000L);
    }

    /**
     * 날짜 유효성 검사 및 23:59:59 setting
     *
     * @param searchDto
     * @return
     */
    public void dateValidator(SearchDto searchDto) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 공백 제거
        searchDto.setSearchStartDate(searchDto.getSearchStartDate().trim());
        searchDto.setSearchEndDate(searchDto.getSearchEndDate().trim());

        String startDateStr = searchDto.getSearchStartDate();
        String endDateStr = searchDto.getSearchEndDate();

        // 입력된 날짜가 있는 경우
        if (!ObjectUtils.isEmpty(startDateStr) && !ObjectUtils.isEmpty(endDateStr)) {
            // 날짜 형식 변환
            LocalDate startDate = LocalDate.parse(startDateStr, dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(endDateStr, dateTimeFormatter);

            // 시작 날짜가 끝나는 날짜를 넘을 경우 false 리턴
            if (startDate.isAfter(endDate)) {
                throw new CustomException(CustomError.DATE_START_DATE_OVER_ERROR); // 시작 날짜보다 이후 날짜를 입력해주세요.
            }

            // local -> UTC로 변경
            searchDto.setSearchStartDate(localTimeToUtc(searchDto.getSearchStartDate() + " 00:00:00"));
            searchDto.setSearchEndDate(localTimeToUtc(searchDto.getSearchEndDate() + " 23:59:59"));

        }
    }


    /**
     * 날짜 유효성 검사 및 23:59:59 setting
     *
     * @param searchDto
     * @return
     */
    public void dateValidator(SearchProductDto searchDto) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 공백 제거
        searchDto.setSearchStartDate(searchDto.getSearchStartDate().trim());
        searchDto.setSearchEndDate(searchDto.getSearchEndDate().trim());

        String startDateStr = searchDto.getSearchStartDate();
        String endDateStr = searchDto.getSearchEndDate();

        // 입력된 날짜가 있는 경우
        if (!ObjectUtils.isEmpty(startDateStr) && !ObjectUtils.isEmpty(endDateStr)) {
            // 날짜 형식 변환
            LocalDate startDate = LocalDate.parse(startDateStr, dateTimeFormatter);
            LocalDate endDate = LocalDate.parse(endDateStr, dateTimeFormatter);

            // 시작 날짜가 끝나는 날짜를 넘을 경우 false 리턴
            if (startDate.isAfter(endDate)) {
                throw new CustomException(CustomError.DATE_START_DATE_OVER_ERROR); // 시작 날짜보다 이후 날짜를 입력해주세요.
            }

            // local -> UTC로 변경
            searchDto.setSearchStartDate(localTimeToUtc(searchDto.getSearchStartDate() + " 00:00:00"));
            searchDto.setSearchEndDate(localTimeToUtc(searchDto.getSearchEndDate() + " 23:59:59"));

        }
    }
}