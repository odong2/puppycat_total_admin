package com.architecture.admin.api.v1.contents;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.contents.ContentsImgDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.contents.ContentsService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/contents")
public class ContentsV1Controller extends BaseController {
    private final ContentsService contentsService;

    /**
     * 콘텐츠 리스트
     *
     * @param searchDto
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity contentsList(@ModelAttribute("param") SearchDto searchDto) {

        // list
        List<ContentsDto> list;

        String sort = searchDto.getSearchSort();

        if (sort.equals("hourPopular")) { // 급상승 인기 게시물
            list = contentsService.getHourPopularList(searchDto);
        } else if (sort.equals("weekPopular")) { // 주간 인기 게시물
            list = contentsService.getWeekPopularList(searchDto);
        } else {
            list = contentsService.getList(searchDto);
        }

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.contents.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 게시글 최근 20개 리스트 by memberUuid
     *
     * @param memberUuid memberUuid
     * @return ResponseEntity
     */
    @GetMapping("/member/view/{memberUuid}")
    public ResponseEntity contentLastTwentyCasesList(@PathVariable("memberUuid") String memberUuid) {
        ContentsDto contentsDto = new ContentsDto();
        contentsDto.setUuid(memberUuid);
        // list
        List<ContentsDto> list = contentsService.getContentLastTwentyCasesListByMemberUuid(contentsDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 이미지 내 태그 회원 검색
     *
     * @param contentsDto nick
     * @return 처리결과
     */
    @GetMapping("/search/{nick}")
    public ResponseEntity searchMember(@ModelAttribute ContentsDto contentsDto, @PathVariable String nick) {

        // 아이디로 멤버 UUID 조회
        String memberUuid = contentsService.searchMember(nick);

        HashMap<String, Object> hmReturnData = new HashMap<>();
        hmReturnData.put("memberUuid", memberUuid);

        // set return JSONObject data
        JSONObject data = new JSONObject(hmReturnData);
        String message;
        if (!ObjectUtils.isEmpty(memberUuid)) {
            message = super.langMessage("lang.contents.search.member.success");
        } else {
            message = super.langMessage("lang.contents.search.member.fail");
        }

        return displayJson(true, "1000", message, data);
    }

    /**
     * 컨텐츠 View
     *
     * @param contentsDto idx
     * @return 처리결과
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity viewContents(@ModelAttribute ContentsDto contentsDto, @RequestParam(value = "viewType", defaultValue = "1") String viewType) {

        contentsDto.setViewType(viewType);

        // 상세 정보
        List<ContentsDto> list = contentsService.getDetailList(contentsDto);

        // 이미지 정보
        List<ContentsImgDto> imgList = contentsService.getDetailImgList(contentsDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("imgList", imgList);

        String sMessage = super.langMessage("lang.contents.success.regist");
        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/{idx}/members")
    public ResponseEntity<String> getMemberList(SearchDto searchDto) {

        String searchType = searchDto.getSearchType();
        List<MemberDto> memberList = new ArrayList<>();

        // SearchType에 맞게 검색
        if (searchType.equals("likeMembers")) {            // 좋아요 한 회원 리스트
            memberList = contentsService.getLikeMemberList(searchDto);
        } else if (searchType.equals("saveMembers")) {     // 저장한 회원 리스트
            memberList = contentsService.getSaveMmemberList(searchDto);
        } else if (searchType.equals("commentsMembers")) { // 댓글 작성한 회원 리스트
            memberList = contentsService.getCommentMemberList(searchDto);
        } else if (searchType.equals("reportMembers")) {   // 신고 회원 리스트
            memberList = contentsService.getReportMemberList(searchDto);
        }

        JSONObject joData = new JSONObject();
        joData.put("list", memberList);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.contents.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 이미지 확대
     *
     * @param imgDto : originUrl, imgWidth, imgHeight
     * @return
     */
    @GetMapping("/image")
    public ResponseEntity<String> getPetBigImg(@ModelAttribute ContentsImgDto imgDto) {
        String imgUrl = contentsService.getContentsBigImg(imgDto);

        JSONObject data = new JSONObject();
        data.put("imgUrl", imgUrl);

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.contents.success.search");

        return displayJson(true, "1000", sMessage, data);

    }

}
