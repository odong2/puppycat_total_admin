package com.architecture.admin.api.v1.member;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.pet.PetDto;
import com.architecture.admin.services.follow.FollowService;
import com.architecture.admin.services.member.MemberPetService;
import com.architecture.admin.services.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member")
public class MemberV1Controller extends BaseController {

    private final MemberService memberService;
    private final MemberPetService memberPetService;
    private final FollowService followService;

    /**
     * 회원 목록
     *
     * @param searchDto searchType, searchWord, searchDateType, searchStartDate, searchEndDate, isDel
     * @return ResponseEntity
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(5);
        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());
        // list
        List<MemberDto> listMember = memberService.getListMember(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listMember);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        String sMessage = "";
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.member.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 상세
     *
     * @param memberIdx memberIdx
     * @return
     */
    @GetMapping("/view/{memberIdx}")
    public ResponseEntity view(@PathVariable(name = "memberIdx") Long memberIdx) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        // 상세
        MemberDto viewMember = memberService.getViewMember(memberIdx);

        Map<String, Object> map = new HashMap<>();
        map.put("member", viewMember);

        // response object
        JSONObject data = new JSONObject(map);
        String sMessage = "";
        if (viewMember == null) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.member.exception.searchFail");

        }
        return displayJson(true, "1000", sMessage, data);
    }

    /**
     * 팔로우/팔로워 랭킹
     *
     * @param searchDto : searchTargetType, searchType, searchWord
     * @return
     */
    @GetMapping("/rank")
    public ResponseEntity<String> getRankList(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(51);

        // list
        List<MemberDto> listMember = memberService.getRankList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", listMember);
        joData.put("params", new JSONObject(searchDto));

        String sMessage = "";

        if (listMember.isEmpty()) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.member.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 팔로우/팔로워 리스트
     *
     * @param memberUuid
     * @param searchDto : searchTargetType, searchType, searchWord
     * @return
     */
    @GetMapping("/{uuid}/follow/list")
    public ResponseEntity<String> getMemberFollowList(@PathVariable(name = "uuid") String memberUuid, SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(51);
        searchDto.setMemberUuid(memberUuid); // uuid set

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        List<MemberDto> followList = followService.getMemberFollowList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", followList);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        String sMessage = "";

        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.member.exception.searchFail");
        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 닉네임 수정시 중복 확인
     *
     * @param memberDto nick
     * @return 사용 가능한 닉네임입니다.
     */
    @GetMapping("/nick/check")
    public ResponseEntity checkNick(@ModelAttribute("MemberDto") MemberDto memberDto) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        // 닉네임 사용여부 체크
        Boolean bIsCheckNick = memberService.checkNick(memberDto);

        if (Boolean.FALSE.equals(bIsCheckNick)) {
            throw new CustomException(CustomError.NICK_CHECK_FAIL);
        }
        // set return data
        JSONObject data = new JSONObject();

        // return value
        String sErrorMessage = "lang.member.success.check.nick";
        String message = super.langMessage(sErrorMessage);
        return displayJson(true, "1000", message, data);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView reportListExcel(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(5);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = memberService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

    /**
     * 최근 교류많은 회원 20명 리스트 by memberUuid
     *
     * @param memberUuid memberUuid
     * @return ResponseEntity
     */
    @GetMapping("/interactive/view/{memberUuid}")
    public ResponseEntity commentLastTwentyCasesList(@PathVariable("memberUuid") String memberUuid) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUuid(memberUuid);
        // list
        List<MemberDto> list = memberService.getInteractiveLastTwentyCasesListByMemberUuid(memberDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.comment.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /****************************************************
     * CURL
     ****************************************************/

    /**
     * 회원 정보 조회
     *
     * @param uuidList
     * @return
     */
    @GetMapping("info")
    public ResponseEntity<String> searchMemberInfo(@RequestParam List<String> uuidList) {
        List<MemberDto> memberInfoList = memberService.getMemberInfo(uuidList);

        JSONObject data = new JSONObject();
        data.put("list", memberInfoList);

        return displayJson(true, "1000", "", data);
    }

    /**
     * 회원 uuid 조회
     *
     *
     * @param searchDto : searchType[id or nick], searchWord, page, limit, recordSize
     * @return
     */
    @GetMapping("/uuid")
    public ResponseEntity<String> searchMemberUuid(SearchDto searchDto) {
        List<String> memberUuid = memberService.getMemberUuid(searchDto);

        JSONObject data = new JSONObject();
        data.put("list", memberUuid);

        return displayJson(true, "1000", "", data);
    }

    /**
     * 펫 리스트 정보 조회
     *
     * @param petUuidList
     * @return
     */
    @GetMapping("/pet-info")
    public ResponseEntity<String> getPetInfo(@RequestParam(name = "petList") List<String> petUuidList) {

        List<PetDto> petList;

        // 반려동물 정보 리스트
        petList = memberPetService.getPetInfoList(petUuidList);


        JSONObject data = new JSONObject();
        data.put("list", petList);

        return displayJson(true, "1000", "success", data);
    }

    /**
     * 회원 uuid 검증
     *
     * @param uuidList
     * @return
     */
    @GetMapping("/uuid/list/check")
    public ResponseEntity<String> checkMemberUuid(@RequestParam(name = "memberUuidList") List<String> uuidList) {

        boolean result = memberService.checkMemberUuidList(uuidList);

        String sMessage = super.langMessage("lang.common.success.search");

        JSONObject data = new JSONObject();
        data.put("isExist", result);

        return displayJson(true, "1000", sMessage, data);
    }
}