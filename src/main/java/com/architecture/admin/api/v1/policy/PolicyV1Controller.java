package com.architecture.admin.api.v1.policy;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.policy.PolicyDto;
import com.architecture.admin.models.dto.product.FileResponseDto;
import com.architecture.admin.services.policy.PolicyService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/policy")
public class PolicyV1Controller extends BaseController {

    private final PolicyService policyService;

    /**
     * 이용약관 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(16);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<PolicyDto> list = policyService.getPolicyList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.policy.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.policy.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 이용약관 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(16);
        // 상세
        PolicyDto viewPolicy = policyService.getViewPolicy(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("policy", viewPolicy);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (viewPolicy == null) {
            sMessage = super.langMessage("lang.policy.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 이용약관 등록
     *
     * @param policyDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity insert(@ModelAttribute("policyDto") PolicyDto policyDto) {
        // 관리자 접근 권한
        super.adminAccessFail(16);

        // 이용약관 등록
        Integer result = policyService.registPolicy(policyDto);

        String sMessage;
        // 등록 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.policy.success.regist");
        }
        // 등록 실패
        else {
            sMessage = super.langMessage("lang.policy.exception.registFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 이용약관 수정
     *
     * @param policyDto
     * @return 수정 완료하였습니다.
     */
    @PutMapping("/{idx}")
    public ResponseEntity modify(@ModelAttribute("PolicyDto") PolicyDto policyDto) {
        // 관리자 접근 권한
        super.adminAccessFail(16);
        // 수정
        int result = policyService.modifyPolicy(policyDto);

        String sMessage;
        // 수정 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.member.success.modify");
        } else {
            sMessage = super.langMessage("lang.member.exception.modifyFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    @GetMapping("/category")
    public ResponseEntity<String> getCategoryList() {
        // 관리자 접근 권한
        super.adminAccessFail(16);

        PolicyDto policyDto = new PolicyDto();
        List<PolicyDto> categoryList = policyService.getPolicyMenuList(policyDto);

        // response object
        JSONObject data = new JSONObject(categoryList);
        data.put("list", categoryList);

        return displayJson(true, "1000", "", data);
    }

    /**
     * 이미지 임시 저장
     *
     * @param response
     * @param image
     * @return
     */
    @PostMapping("/../images/jstree/throbber.gif")
    public ResponseEntity<FileResponseDto> fileUploadFromCKEditor(HttpServletResponse response, @RequestPart(value = "upload", required = false) List<MultipartFile> image) {

        // 이미지 s3 임시 저장
        return new ResponseEntity<>(FileResponseDto.builder().
                uploaded(true).
                url(policyService.tempImage(image)).
                build(), HttpStatus.OK);
    }

}
