package com.architecture.admin.controllers.login;

import com.architecture.admin.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/join")
public class JoinController extends BaseController {

    /**
     * 관리자 가입 페이지
     *
     * @return 가입페이지
     */
    @GetMapping("")
    public String join() {
        // view pages
        hmImportFile.put("importJs", "login/join.js.html");
        return "login/join";
    }

}
