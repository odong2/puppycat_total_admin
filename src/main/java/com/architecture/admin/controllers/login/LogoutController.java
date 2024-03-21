package com.architecture.admin.controllers.login;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.services.login.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/logout")
public class LogoutController extends BaseController {

    private final LogoutService logoutService;

    /**
     * 로그아웃
     *
     * @return 로그인페이지로 이동
     */
    @GetMapping("")
    public String logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        boolean logout = logoutService.logout(httpRequest, httpResponse);
        return "redirect:" + hmServer.get("currentDomain") + "/login";
    }
}
