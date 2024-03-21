package com.architecture.admin.api.v1.faq;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.faq.FaqDto;
import com.architecture.admin.models.dto.product.FileResponseDto;
import com.architecture.admin.services.faq.FaqService;
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
@RequestMapping("/v1/faq")
public class FaqV1Controller extends BaseController {

    private final FaqService faqService;

    /**
     * FAQ 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(15);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<FaqDto> list = faqService.getFaqList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.faq.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.faq.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * FAQ 등록
     *
     * @param faqDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity insert(@ModelAttribute("faqDto") FaqDto faqDto) {
        // 관리자 접근 권한
        super.adminAccessFail(15);

        // FAQ 등록
        Integer result = faqService.registFaq(faqDto);

        String sMessage;
        // 등록 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.faq.success.regist");
        }
        // 등록 실패
        else {
            sMessage = super.langMessage("lang.faq.exception.registFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * FAQ 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(15);
        // 상세
        FaqDto viewFaq = faqService.getViewFaq(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("faq", viewFaq);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (viewFaq == null) {
            sMessage = super.langMessage("lang.faq.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * FAQ 수정
     *
     * @param faqDto
     * @return 수정 완료하였습니다.
     */
    @PutMapping("/{idx}")
    public ResponseEntity modify(@ModelAttribute("FaqDto") FaqDto faqDto) {
        // 관리자 접근 권한
        super.adminAccessFail(15);
        // 수정
        int result = faqService.modifyFaq(faqDto);

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

    /**
     * 이미지 임시 저장
     *
     * @param response
     * @param image
     * @return
     */
    @PostMapping("/tempImage")
    public ResponseEntity<FileResponseDto> fileUploadFromCKEditor(HttpServletResponse response, @RequestPart(value = "upload", required = false) List<MultipartFile> image) {

        // 이미지 s3 임시 저장
        return new ResponseEntity<>(FileResponseDto.builder().
                uploaded(true).
                url(faqService.tempImage(image)).
                build(), HttpStatus.OK);
    }

}
