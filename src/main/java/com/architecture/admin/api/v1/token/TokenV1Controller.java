package com.architecture.admin.api.v1.token;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.libraries.jwt.JwtDto;
import com.architecture.admin.models.daosub.admin.AdminDaoSub;
import com.architecture.admin.services.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/token")
public class TokenV1Controller extends BaseController {

    private final AdminDaoSub adminDaoSub;
    private final MenuService menuService;

    /**
     * 토큰 & 권한 체크
     *
     * @param token
     * @param menuIdx
     * @return
     */
    @GetMapping("")
    public ResponseEntity getTokenValidation(@RequestHeader(value = "Authorization") String token,
                                             @RequestParam(name = "menuIdx", defaultValue = "") Integer menuIdx) {

        // 권한 체크
        if (!ObjectUtils.isEmpty(menuIdx)) {
            // 토큰에서 관리자 id 조회
            String adminId = super.getAccessAdminId(token);
            // 관리자 level 조회
            Integer adminLevel = adminDaoSub.getAdminLevel(adminId);
            // 메뉴 level 조회
            Integer menuLevel = menuService.getMenuLevel(menuIdx);

            if (adminLevel < menuLevel) {
                // 접근 권한이 없습니다
                throw new CustomException(CustomError.ACCESS_FAIL);
            }
        }

        // 토큰 체크
        JwtDto jwtDto = new JwtDto();
        jwtDto.setAccessToken(token);

        Integer validateAccess = jwtLibrary.validateAccessToken(jwtDto);
        if (validateAccess != 1) {
            // 유효 기간이 만료 되어 토큰 검증에 실패 하였습니다.
            throw new CustomException(CustomError.TOKEN_EXPIRE_TIME_ERROR);
        }

        String sMessage = super.langMessage("lang.common.success.search");

        return displayJson(true, "1000", sMessage);
    }

}
