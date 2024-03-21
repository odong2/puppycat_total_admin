package com.architecture.admin.api.v1.comment;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.comment.CommentDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/comment")
public class CommentV1Controller extends BaseController {

    private final CommentService commentService;

    /**
     * 댓글 목록
     *
     * @param searchDto
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(21);

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // list
        List<CommentDto> list = commentService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.common.success.search");

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.common.search.fail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 댓글 최근 20개 리스트 by memberUuid
     *
     * @param memberUuid memberUuid
     * @return ResponseEntity
     */
    @GetMapping("/member/view/{memberUuid}")
    public ResponseEntity commentLastTwentyCasesList(@PathVariable("memberUuid") String memberUuid) {
        CommentDto commentDto = new CommentDto();
        commentDto.setMemberUuid(memberUuid);
        // list
        List<CommentDto> list = commentService.getCommentLastTwentyCasesListByMemberUuid(commentDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.comment.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 댓글 상세 by idx
     *
     * @param commentDto 댓글 idx
     * @return ResponseEntity
     */
    @GetMapping("/view/{idx}")
    public ResponseEntity view(@ModelAttribute CommentDto commentDto, @RequestParam(value="viewType", defaultValue = "") String viewType) {

        commentDto.setViewType(viewType);

        CommentDto viewComment = commentService.getView(commentDto);

        Map<String, Object> map = new HashMap<>();
        map.put("comment", viewComment);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = super.langMessage("lang.common.success.search");

        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 회원 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("{idx}/members")
    public ResponseEntity<String> getMemberList(SearchDto searchDto) {

        String searchType = searchDto.getSearchType();
        List<MemberDto> memberList = new ArrayList<>();

        // SearchType에 맞게 검색
        if (searchType.equals("likeMembers")) {            // 좋아요 한 회원 리스트
            memberList = commentService.getLikeMemberList(searchDto);
        } else if (searchType.equals("commentsMembers")) { // 댓글 작성한 회원 리스트
            memberList = commentService.getCommentMemberList(searchDto);
        } else if (searchType.equals("reportMembers")) {   // 신고 회원 리스트
            memberList = commentService.getReportMemberList(searchDto);
        }

        JSONObject joData = new JSONObject();
        joData.put("list", memberList);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();

        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.common.success.search");

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.common.search.fail");
        }

        return displayJson(true, "1000", sMessage, joData);

    }
}
