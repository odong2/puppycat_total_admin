package com.architecture.admin.api.v1.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.partner.PartnerHolidayDto;
import com.architecture.admin.services.partner.PartnerCalendarService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/partner/holidays")
public class PartnerCalendarV1Controller extends BaseController {

    private final PartnerCalendarService calendarService;

    /**
     * 국가 공휴일 조회
     *
     * @param searchYear : 조회 년도
     * @return : holidayList
     */
    @GetMapping("")
    public ResponseEntity<String> calendar(@RequestParam String searchYear) {

        super.adminAccessFail(16);

        // 국가 공휴일 조회
        List<PartnerHolidayDto> holidayList = calendarService.getHolidayList(searchYear);

        JSONObject data = new JSONObject();
        data.put("holidayList", holidayList);               // 파트너 & 국가 공휴일 리스트

        // return value
        String message = super.langMessage("lang.admin.success.search");// 조회 완료하였습니다.

        return displayJson(true, "1000", message, data);
    }

    /**
     * 휴무일 등록 전 날짜 유효성 검사
     *
     * @param holidayDto : startDate, endDate
     * @return : message
     */
    @GetMapping("/validate")
    public ResponseEntity<String> dateValidate(PartnerHolidayDto holidayDto) {

        super.adminAccessFail(16);

        // 날짜 유효성 검사
        calendarService.dateValidate(holidayDto);

        // return value
        String message = super.langMessage("lang.admin.success.search"); // 조회 완료하였습니다

        return displayJson(true, "1000", message);
    }
}
