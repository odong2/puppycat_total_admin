package com.architecture.admin.api.v1.auth;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*****************************************************
 * auth Token API
 ****************************************************/
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthV1ApiController extends BaseController {
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> authCheck(@RequestBody AdminDto adminDto,
                                       HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws Exception {

        // 회원 로그인 처리
        Boolean bAuthCheck = loginService.bAuthCheck(adminDto);
        String sMessage = "";

        if (bAuthCheck) {
            return displayJson(true, "1000", sMessage);
        }

        return displayJson(false, "9999", sMessage);
    }

}
