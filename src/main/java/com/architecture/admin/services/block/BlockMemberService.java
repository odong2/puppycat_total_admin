package com.architecture.admin.services.block;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.block.BlockMemberDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.block.BlockMemberDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockMemberService extends BaseService {

    private final BlockMemberDaoSub blockMemberDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 회원 차단 목록
     *
     * @param searchDto 검색조건
     * @return 회원 차단 리스트
     */
    public List<BlockMemberDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // 목록 전체 count
        int totalCount = blockMemberDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<BlockMemberDto> list = blockMemberDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // get Lists
        List<BlockMemberDto> blockMemberList = blockMemberDaoSub.getList(searchDto);

        // 상태값 문자 변환
        stateText(blockMemberList);

        // 엑셀 데이터 생성
        return excelData.createExcelData(blockMemberList, BlockMemberDto.class);

    }

    /**
     * 회원 최근 차단 내역 20개 리스트 by memberUuid
     *
     * @param blockMemberDto memberUuid
     * @return list
     */
    public List<BlockMemberDto> getBlockLastTwentyCasesListByMemberUuid(BlockMemberDto blockMemberDto){
        List<BlockMemberDto> list = blockMemberDaoSub.getBlockLastTwentyCasesListByMemberUuid(blockMemberDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

    /*****************************************************
     *  SubFunction - State
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<BlockMemberDto> list) {
        for (BlockMemberDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(BlockMemberDto dto) {
        // 회원 상태
        if (dto.getMemberState() != null && dto.getMemberState() == 1) { // 탈퇴 대기
            dto.setMemberStateText(super.langMessage("lang.block.state.out.wait"));
            dto.setMemberStateBg("badge-danger");
        }
        else{ // 정상
            dto.setMemberStateText(super.langMessage("lang.block.state.normal"));
            dto.setMemberStateBg("badge-success");
        }

        // 차단 회원 상태
        if (dto.getBlockState() != null && dto.getBlockState() == 1) { // 탈퇴 대기
            dto.setBlockStateText(super.langMessage("lang.block.state.out.wait"));
            dto.setBlockStateBg("badge-danger");
        } else{ // 정상
            dto.setBlockStateText(super.langMessage("lang.block.state.normal"));
            dto.setBlockStateBg("badge-success");
        }
    }
}
