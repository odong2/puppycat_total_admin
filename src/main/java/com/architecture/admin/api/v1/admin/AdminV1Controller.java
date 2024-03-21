package com.architecture.admin.api.v1.admin;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin")
public class AdminV1Controller extends BaseController {

    private final AdminService adminService;

    /**
     * 관리자 목록
     *
     * @param searchDto
     * @return 관리자 목록
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(3);

        // list
        List<AdminDto> listAdmin = adminService.getListAdmin(searchDto);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", listAdmin);
        map.put("params", searchDto);

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.admin.exception.search_fail");

        }
        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 관리자 상세
     *
     * @param idx (`sns_admin`.`idx`)
     * @return 관리자 상세
     */
    @GetMapping(value = "/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(3);
        // 상세
        AdminDto viewAdmin = adminService.getViewAdmin(idx);

        // response object
        JSONObject data = new JSONObject(viewAdmin);

        String sMessage = "";
        if (viewAdmin == null) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.admin.exception.search_fail");

        }
        return displayJson(true, "1000", sMessage, data);
    }
    
    
    /**
     * 내 정보 수정
     * @param adminDto
     * @return
     */
    @PutMapping("/mypage/{idx}")
    public ResponseEntity modifyAdminMyPage(@ModelAttribute("adminDto") AdminDto adminDto) {

        // 내 정보 수정
        int result = adminService.modifyAdminMyPage(adminDto);

        String sMessage = "";

        if (result > 0) { // 수정 완료
            sMessage = super.langMessage("lang.admin.success.modify");
        } else { // 수정 실패
            sMessage = super.langMessage("lang.admin.exception.modify_fail");
        }

        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 비밀번호 변경
     * @param adminDto
     * @return
     */
    @PutMapping("/mypage/password/{idx}")
    public ResponseEntity modifyPassword(@PathVariable(name = "idx", required = false) int idx,
                                 @ModelAttribute("adminDto") AdminDto adminDto) {

        // 관리자 접근 권한
        super.adminAccessFail(7);

        // 비밀번호 변경
        adminDto.setIdx(idx); // 관리자 idx set
        int result = adminService.modifyAdminPassword(adminDto);

        String sMessage = "";

        if (result > 0) { // 수정 완료
            sMessage = super.langMessage("lang.admin.success.modify");
        } else { // 수정 실패
            sMessage = super.langMessage("lang.admin.exception.modify_fail");
        }

        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }
    

    /**
     * 관리자 idx 조회
     *
     * @param token
     * @return
     */
    @GetMapping("/info")
    public AdminDto getAdminInfoByToken(@RequestHeader(value = "Authorization") String token) {

        String adminId = super.getAccessAdminId(token); // 회원 id 조회

        if (ObjectUtils.isEmpty(adminId)) {
            throw new CustomException(CustomError.TOKEN_ERROR);
        }

        Integer adminIdx = adminService.getAdminIdxById(adminId); // 관리자 idx 조회

        AdminDto adminDto = new AdminDto();
        adminDto.setResult(true);
        adminDto.setCode("1000");
        adminDto.setId(adminId);
        adminDto.setIdx(adminIdx);

        return adminDto;
    }

    /**
     * 관리자 수정
     *
     * @param adminDto (idx : `sns_admin`.`idx`)
     * @return 관리자 수정
     */
    @PutMapping("/{idx}")
    public ResponseEntity modify(@ModelAttribute("adminDto") AdminDto adminDto) {
        // 관리자 접근 권한
        super.adminAccessFail(3);

        // 수정
        int result = adminService.modifyAdmin(adminDto);

        String sMessage = "";
        // 수정 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.admin.success.modify");
        }
        // 수정 실패
        else {
            sMessage = super.langMessage("lang.admin.exception.modify_fail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 관리자 ID 조회
     *
     * @param token
     * @return
     */
    @GetMapping("/id")
    public ResponseEntity getAdminIdByToken(@RequestHeader(value = "Authorization") String token) {

        // 토큰에서 회원 UUID 가져오기
        String adminId = super.getAccessAdminId(token);
        if (ObjectUtils.isEmpty(adminId)) {
            throw new CustomException(CustomError.TOKEN_ERROR);
        }

        String sMessage = super.langMessage("lang.common.success.search");

        JSONObject data = new JSONObject();
        data.put("adminId", adminId);
        return displayJson(true, "1000", sMessage, data);
    }

}
