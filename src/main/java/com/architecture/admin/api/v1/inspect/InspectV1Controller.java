package com.architecture.admin.api.v1.inspect;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.inspect.InspectDto;
import com.architecture.admin.models.dto.inspect.UpdateInspectDto;
import com.architecture.admin.services.inspect.InspectService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/inspect")
public class InspectV1Controller extends BaseController {

    private final InspectService inspectService;

    /**
     * 점검 정보
     *
     * @return 점검 정보
     */
    @GetMapping("/list")
    public ResponseEntity getInspectInfo() throws IOException {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        // Ojbect Init
        JSONObject joData = new JSONObject();
        String code;
        String sMessage;

        // get curl list
        String list = inspectService.getInspectInfo();

        if (list != null) {
            JSONObject jList = new JSONObject(list);
            joData.put("list", jList);
            code = "1000";
            sMessage = super.langMessage("lang.inspect.update.success.search");
        } else {
            joData.put("list", new JSONObject());
            code = "0";
            sMessage = super.langMessage("lang.inspect.update.list.empty");
        }


        return displayJson(true, code, sMessage, joData);
    }

    /**
     * 점검 정보 등록
     *
     * @return 점검 정보 등록
     */
    @PostMapping("/regist")
    public ResponseEntity regist(@ModelAttribute("InspectDto") InspectDto inspectDto) throws IOException, ParseException {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        // 점검 업데이트 정보 등록
        inspectService.registInspect(inspectDto);

        String sMessage = super.langMessage("lang.inspect.update.regist.success");

        return displayJson(true, "1000", sMessage);
    }

    /**
     * 점검 업데이트 정보
     *
     * @return 업데이트 정보
     */
    @GetMapping("/update/list")
    public ResponseEntity getUpdateInfo() throws IOException {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        // Ojbect Init
        JSONObject joData = new JSONObject();
        String code;
        String sMessage;
        // get culr list
        String list = inspectService.getUpdateInspectInfo();

        if (list != null) {
            JSONObject jList = new JSONObject(list);
            joData.put("list", jList);
            code = "1000";
            sMessage = super.langMessage("lang.inspect.update.success.search");
        } else {
            joData.put("list", new JSONObject());
            code = "0";
            sMessage = super.langMessage("lang.inspect.update.list.empty");
        }

        return displayJson(true, code, sMessage, joData);
    }

    /**
     * 점검 업데이트 정보 등록
     *
     * @return 업데이트 정보 등록
     */
    @PostMapping("/update/regist")
    public ResponseEntity updateRegist(@ModelAttribute("updateInspectDto") UpdateInspectDto updateInspectDto) throws IOException {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        // 점검 업데이트 정보 등록
        inspectService.registUpdateInspect(updateInspectDto);

        String sMessage = super.langMessage("lang.inspect.update.regist.success");

        return displayJson(true, "1000", sMessage);
    }
}