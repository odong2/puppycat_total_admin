package com.architecture.admin.api.v1.login;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.login.JoinService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/join")
public class JoinV1Controller extends BaseController {

    private final JoinService joinService;

    /**
     * 관리자 등록
     *
     * @param adminDto (id / password / passwordConfirm)
     * @param result
     * @return 가입되었습니다.
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity join(@Valid AdminDto adminDto, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            return super.displayError(result, 400);
        }

        // 아이디 중복체크
        Boolean bDuple = joinService.checkDupleId(adminDto);
        if (Boolean.TRUE.equals(bDuple)) { // 증복된 아이디인 경우
            throw new CustomException(CustomError.ID_DUPLE);
        }

        // 관리자 등록
        joinService.regist(adminDto);

        // set return data
        JSONObject data = new JSONObject();
        data.put("location", "/login");

        // return value
        String sErrorMessage = "lang.admin.success.regist";
        String message = super.langMessage(sErrorMessage);
        return displayJson(true, "1000", message, data);
    }

}
