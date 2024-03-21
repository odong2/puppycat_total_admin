package com.architecture.admin.api.v1.notice;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.NoticeDto;
import com.architecture.admin.models.dto.product.FileResponseDto;
import com.architecture.admin.services.notice.NoticeService;
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
@RequestMapping("/v1/notice")
public class NoticeV1Controller extends BaseController {

    private final NoticeService noticeService;
    private final S3Library s3Library;

    /**
     * 공지사항 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(14);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<NoticeDto> list = noticeService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.notice.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.notice.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 공지사항 상세
     *
     * @param idx
     * @return
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@PathVariable(name = "idx", required = false) Integer idx) {
        // 관리자 접근 권한
        super.adminAccessFail(14);
        // 상세
        NoticeDto viewNotice = noticeService.getViewNotice(idx);

        Map<String, Object> map = new HashMap<>();
        map.put("notice", viewNotice);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (viewNotice == null) {
            sMessage = super.langMessage("lang.notice.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, data);
    }


    /**
     * 공지사항 등록
     *
     * @param noticeDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity insertNotice(@RequestHeader("Authorization") String token,
                                       @ModelAttribute("noticeDto") NoticeDto noticeDto) {
        // 관리자 접근 권한
        super.adminAccessFail(14);

        // 공지사항 등록
        Integer result = noticeService.registNotice(token, noticeDto);

        String sMessage;
        // 등록 완료
        if (result > 0) {
            sMessage = super.langMessage("lang.notice.success.regist");
        }
        // 등록 실패
        else {
            sMessage = super.langMessage("lang.notice.exception.registFail");
        }
        // response object
        JSONObject data = new JSONObject(result);

        return displayJson(true, "1000", sMessage, data);
    }


    /**
     * 공지사항 수정
     *
     * @param noticeDto
     * @return 수정 완료하였습니다.
     */
    @PutMapping("/{idx}")
    public ResponseEntity modifyNotice(@ModelAttribute("NoticeDto") NoticeDto noticeDto) {
        // 관리자 접근 권한
        super.adminAccessFail(14);
        // 수정
        int result = noticeService.modifyNotice(noticeDto);

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
    public ResponseEntity fileUploadFromCKEditor(HttpServletResponse response, @RequestPart(value = "upload", required = false) List<MultipartFile> image) {
        // 이미지 s3 임시 저장
        return new ResponseEntity<>(FileResponseDto.builder().
                uploaded(true).
                url(noticeService.tempImage(image)).
                build(), HttpStatus.OK);
    }
}
