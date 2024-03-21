package com.architecture.admin.api.v1.ip;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.ip.IpDto;
import com.architecture.admin.services.ip.IpService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/ip")
public class IpV1Controller extends BaseController {

    private final IpService ipService;

    /**
     * 관리자 허용 IP 리스트 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView paymentMethodListExcelDownload(@ModelAttribute("param") SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = ipService.excelAdminIp(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 관리자 허용 IP 리스트 조회
     *
     * @param searchDto
     */
    @GetMapping()
    public ResponseEntity getAdminIpList(@ModelAttribute SearchDto searchDto) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // IP 리스트 조회
        JSONObject joData = ipService.getAdminIpList(searchDto);

        // 결과 메세지 처리
        String sMessage = super.langMessage("lang.ip.success.search"); // 검색을 완료하였습니다.

        // 조회 결과가 없는 경우
        if (joData.getJSONArray("list").isEmpty()) {
            sMessage = super.langMessage("lang.ip.exception.search.fail"); // 검색 결과가 없습니다.
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 관리자 허용 IP 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/{idx}")
    public ResponseEntity getViewAdminIp(@PathVariable(name = "idx") Long idx) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // IP 상세
        JSONObject joData = ipService.getViewAdminIp(idx);

        // 결과 메세지 처리
        String sMessage = super.langMessage("lang.ip.success.search"); // 검색을 완료하였습니다.

        // 조회 결과가 없는 경우
        if (joData.isEmpty()) {
            sMessage = super.langMessage("lang.ip.exception.search.fail"); // 검색 결과가 없습니다.
        }
        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 관리자 허용 IP 등록
     *
     * @param ipDto
     * @return
     */
    @PostMapping()
    public ResponseEntity insertAdminIp(@ModelAttribute("IpDto") IpDto ipDto) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // IP 등록
        ipService.insertAdminIp(ipDto);

        // 결과 메세지 처리
        String sMessage = super.langMessage("lang.ip.success.register"); // 등록을 완료하였습니다.

        return displayJson(true, "1000", sMessage);
    }

    /**
     * 관리자 허용 IP 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteAdminIp(@PathVariable(name = "idx") Long idx) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // IP 삭제
        ipService.deleteAdminIp(idx);

        // 결과 메세지 처리
        String sMessage = super.langMessage("lang.ip.success.delete"); // 삭제를 완료하였습니다.

        return displayJson(true, "1000", sMessage);
    }

    /**
     * 관리자 허용 IP 수정
     *
     * @param idx
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity updateAdminIp(@PathVariable(name = "idx") Long idx,
                                        @ModelAttribute("IpDto") IpDto ipDto) {

        // 관리자 접근 권한
        super.adminAccessFail(56);

        // idx set
        ipDto.setIdx(idx);

        // IP 수정
        ipService.updateAdminIp(ipDto);

        // 결과 메세지 처리
        String sMessage = super.langMessage("lang.payment.method.success.modify"); // 수정을 완료하였습니다.

        return displayJson(true, "1000", sMessage);
    }
}
