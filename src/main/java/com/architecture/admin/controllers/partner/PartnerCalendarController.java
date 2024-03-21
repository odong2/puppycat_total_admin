package com.architecture.admin.controllers.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.partner.PartnerHolidayDto;
import com.architecture.admin.services.partner.PartnerCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner/calendar")
public class PartnerCalendarController extends BaseController {

    private final PartnerCalendarService calendarService;

    /**
     * 캘린더 조호
     *
     * @return : 공휴일 페이지
     */
    @GetMapping("")
    public String calendar() {

        super.adminAccessFail(16);

        // view pages
        hmImportFile.put("importJs", "partner/calendar/list.js.html");

        return "partner/calendar/list";
    }


    /**
     * 공휴일 등록 페이지
     *
     * @param model             : startDate, endDate
     * @param partnerHolidayDto : startDate, endDate
     * @return : 공휴일 등록 페이지
     */
    @GetMapping("/regist")
    public String registCalendar(Model model, PartnerHolidayDto partnerHolidayDto) {

        super.adminAccessFail(16);

        String startDate = partnerHolidayDto.getStartDate();
        String endDate = partnerHolidayDto.getEndDate();

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        // view pages
        hmImportFile.put("importJs", "partner/calendar/regist.js.html");

        return "partner/calendar/regist";
    }

    /**
     * 국가 공휴일 수정 페이지
     *
     * @param model : holidayInfo
     * @param idx   : idx
     * @return : 공휴일 수정 페이지
     */
    @GetMapping("/{idx}")
    public String modifyCalendar(Model model, @PathVariable Integer idx) {

        super.adminAccessFail(16);

        PartnerHolidayDto holidayDto = calendarService.getHolidayInfoByIdx(idx);

        model.addAttribute("holidayInfo", holidayDto);

        // view pages
        hmImportFile.put("importJs", "partner/calendar/modify.js.html");

        return "partner/calendar/modify";
    }

}
