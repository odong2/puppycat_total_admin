package com.architecture.admin.api.v1.block;

import com.architecture.admin.controllers.BaseController;
import com.architecture.admin.libraries.excel.ExcelXlsxView;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.block.BlockMemberDto;
import com.architecture.admin.services.block.BlockMemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/block/members")
public class BlockMemberV1Controller extends BaseController {

    private final BlockMemberService blockMemberService;

    /**
     * 차단 리스트
     *
     * @param searchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity lists(@ModelAttribute("param") SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(9);

        // list
        List<BlockMemberDto> list = blockMemberService.getList(searchDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);
        joData.put("params", new JSONObject(searchDto));

        // total count
        int totalCount = searchDto.getPagination().getTotalRecordCount();
        //검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.block.success.search");
        if (totalCount < 1) {
            // 검색 결과가 없습니다.
            sMessage = super.langMessage("lang.block.exception.searchFail");

        }

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 회원 차단 내역 최근 20개 리스트 by memberUuid
     *
     * @param memberUuid memberUuid
     * @return ResponseEntity
     */
    @GetMapping("/view/{memberUuid}")
    public ResponseEntity commentLastTwentyCasesList(@PathVariable("memberUuid") String memberUuid) {
        BlockMemberDto blockMemberDto = new BlockMemberDto();
        blockMemberDto.setMemberUuid(memberUuid);
        // list
        List<BlockMemberDto> list = blockMemberService.getBlockLastTwentyCasesListByMemberUuid(blockMemberDto);

        JSONObject joData = new JSONObject();
        joData.put("list", list);

        // 검색 완료 하였습니다.
        String sMessage = super.langMessage("lang.comment.success.search");

        return displayJson(true, "1000", sMessage, joData);
    }

    /**
     * 엑셀 다운로드
     *
     * @return
     */
    @GetMapping("/excel")
    public ModelAndView excelDownload(SearchDto searchDto) {
        // 관리자 접근 권한
        super.adminAccessFail(9);

        // 뷰에 담을 모델객체를 생성(엑셀 파일에 들어갈 데이터)
        Map<String, Object> mExcelData = blockMemberService.excelDownload(searchDto);

        // AbstractXlsxView 가 동작
        return new ModelAndView(new ExcelXlsxView(), mExcelData);
    }

}
