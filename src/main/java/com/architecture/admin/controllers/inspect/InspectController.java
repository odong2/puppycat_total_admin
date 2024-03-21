package com.architecture.admin.controllers.inspect;

import com.architecture.admin.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/inspect")
public class InspectController extends BaseController {

    /**
     * 점검 리스트
     * @return 점검 리스트 페이지
     */
    @GetMapping("/list")
    public String lists() {
        // 관리자 접근 권한
        super.adminAccessFail(57);

        hmImportFile.put("importJs", "inspect/list.js.html");

        return "inspect/list";
    }

    /**
     * 점검 등록
     * @return 점검 등록 페이지
     */
    @GetMapping("/regist")
    public String regist() {
        // 관리자 접근 권한
        super.adminAccessFail(57);

        hmImportFile.put("importJs", "inspect/regist.js.html");

        return "inspect/regist";
    }

    /**
     * 업데이트 점검 리스트
     * @return 업데이트 점검 리스트 페이지
     */
    @GetMapping("/update/list")
    public String updateLists() {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        //page load
        hmImportFile.put("importJs", "inspect.update/list.js.html");

        return "inspect.update/list";
    }

    /**
     * 업데이트 점검 등록
     * @return 업데이트 점검 등록
     */
    @GetMapping("/update/regist")
    public String updateRegist() {

        // 관리자 접근 권한
        super.adminAccessFail(55);

        hmImportFile.put("importJs", "inspect.update/regist.js.html");

        return "inspect.update/regist";
    }
}
