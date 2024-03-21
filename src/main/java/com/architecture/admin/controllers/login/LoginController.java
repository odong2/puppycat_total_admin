package com.architecture.admin.controllers.login;

import com.architecture.admin.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    /**
     * 관리자 로그인 페이지
     * 
     * @return 관리자 로그인 페이지
     */
    @GetMapping("")
    public String login() {
        // view pages
        hmImportFile.put("importJs", "login/login.js.html");
        return "login/login";
    }

}
