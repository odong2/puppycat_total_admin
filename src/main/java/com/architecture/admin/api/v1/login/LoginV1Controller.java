package com.architecture.admin.api.v1.login;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/login")
public class LoginV1Controller extends BaseController {

    private final LoginService loginService;

    /**
     * 로그인 세션 없이 페이지 호출 시 처리
     *
     * @return
     */
    @RequestMapping("/session")
    public ResponseEntity lostSession() {

        // set return data
        JSONObject data = new JSONObject();

        // return value
        String sErrorMessage = "lang.admin.exception.login.again";
        String message = super.langMessage(sErrorMessage);
        return displayJson(false, "9999", message, data);
    }

    /**
     * 관리자 로그인
     *
     * @param adminDto     (id / password)
     * @param result
     * @param httpRequest
     * @param httpResponse
     * @return
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity login(@Valid AdminDto adminDto,
                                BindingResult result,
                                HttpServletRequest httpRequest,
                                HttpServletResponse httpResponse) throws Exception {

        if (result.hasErrors()) {
            return super.displayError(result, 400);
        }

        // 회원 로그인 처리
        Boolean bIsLogin = loginService.login(adminDto, httpRequest, httpResponse);

        if (Boolean.FALSE.equals(bIsLogin)) {
            throw new CustomException(CustomError.LOGIN_FAIL);
        }

        JSONObject data = new JSONObject();
        data.put("location", "/");
        // return value
        String sErrorMessage = "lang.admin.success.login";
        String message = super.langMessage(sErrorMessage);
        return displayJson(true, "1000", message, data);
    }
}
