package com.architecture.admin.controllers.partner;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.bank.BankDto;
import com.architecture.admin.models.dto.partner.PartnerBankDto;
import com.architecture.admin.models.dto.partner.PartnerDto;
import com.architecture.admin.models.dto.partner.PartnerImgDto;
import com.architecture.admin.models.dto.partner.PartnerManagerDto;
import com.architecture.admin.services.bank.BankService;
import com.architecture.admin.services.partner.PartnerService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController extends BaseController {
    private final PartnerService partnerService;
    private final BankService bankService;

    /**
     * 파트너 리스트
     *
     * @return
     */
    @GetMapping("/list")
    public String partnerList(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @ModelAttribute SearchDto searchDto) {
        // 파트너 레벨 체크
        super.adminAccessFail(72);

        // 회원가입 시 입력한 파트너 정보 조회
        model.addAttribute("page", page);
        model.addAttribute("search", searchDto);

        // view pages
        hmImportFile.put("importJs", "partner/list.js.html");

        return "partner/list";
    }

    /**
     * 파트너 등록 페이지
     *
     * @return
     */
    @GetMapping("/regist")
    public String partnerRegist() {
        // 파트너 레벨 체크
        super.adminAccessFail(72);

        // view pages
        hmImportFile.put("importJs", "partner/regist.js.html");

        return "partner/regist";
    }

    /**
     * 파트너 수정 페이지
     *
     * @return
     */
    @GetMapping("/modify/{idx}")
    public String partnerModify(@PathVariable Integer idx,
                                Model model) {
        // 파트너 레벨 체크
        super.adminAccessFail(72);
        // 회원가입 시 입력한 파트너 정보
        PartnerDto partnerInfo = partnerService.getPartnerDetail(idx);
        // 파트너 이미지 파일 조회
        List<PartnerImgDto> imgList = partnerService.getPartnerImgList(idx);
        // 은행 리스트 조회(select box)
        List<BankDto> bankList = bankService.getBankList();
        // 파트너 은행 정보
        PartnerBankDto partnerBankInfo = partnerService.getPartnerBankInfo(idx);

        // 리스트 담을 json object
        JSONObject jsonData = new JSONObject();
        jsonData.put("imgList", imgList);

        model.addAttribute("partnerInfo", partnerInfo);
        model.addAttribute("partnerBankInfo", partnerBankInfo);
        model.addAttribute("bankList", bankList);
        model.addAttribute("jsonData", jsonData);
        model.addAttribute("idx", idx);

        // view pages
        hmImportFile.put("importJs", "partner/modify.js.html");

        return "partner/modify";
    }

    /**
     * 파트너 상세
     *
     * @return
     */
    @GetMapping("/view/{idx}")
    public String partnerDetail(@PathVariable Integer idx,
                                Model model) {
        // 파트너 레벨 체크
        super.adminAccessFail(72);
        // 회원가입 시 입력한 파트너 정보
        PartnerDto partnerInfo = partnerService.getPartnerDetail(idx);
        // 파트너 이미지 파일 조회
        List<PartnerImgDto> imgList = partnerService.getPartnerImgList(idx);
        // 파트너 은행 정보
        PartnerBankDto partnerBankInfo = partnerService.getPartnerBankInfo(idx);
        // 매니저 조회
        List<PartnerManagerDto> managerList = partnerService.getPartnerManagerList(idx);

        // 리스트 담을 json object
        JSONObject jsonData = new JSONObject();
        jsonData.put("imgList", imgList);
        jsonData.put("managerList", managerList);

        model.addAttribute("partnerInfo", partnerInfo);
        model.addAttribute("partnerBankInfo", partnerBankInfo);
        model.addAttribute("jsonData", jsonData);
        model.addAttribute("idx", idx);

        // view pages
        hmImportFile.put("importJs", "partner/view.js.html");

        return "partner/view";
    }

}
