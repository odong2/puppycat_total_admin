package com.architecture.admin.api.v1.follow;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.follow.FollowDto;
import com.architecture.admin.services.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/follow")
public class FollowV1Controller extends BaseController {

    private final FollowService followService;

    /**
     * 팔로우 목록
     *
     * @param searchDto
     * @return 검색 성공
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(8);
        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<FollowDto> list = followService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.follow.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.follow.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView followListExcel(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(8);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = followService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 회원 팔로잉 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    @PostMapping("/lists/following")
    public ResponseEntity followingLists(@ModelAttribute("FollowDto") FollowDto followDto) {
        // list
        List<FollowDto> list = followService.getFollowingListByMemberUuid(followDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.follow.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 팔로워 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    @PostMapping("/lists/follower")
    public ResponseEntity followerLists(@ModelAttribute("FollowDto") FollowDto followDto) {
        // list
        List<FollowDto> list = followService.getFollowerListByMemberUuid(followDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.follow.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }
}
