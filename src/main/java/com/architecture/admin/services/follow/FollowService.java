package com.architecture.admin.services.follow;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.follow.FollowDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.follow.FollowDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/*****************************************************
 * 팔로우 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class FollowService extends BaseService {
    private final FollowDaoSub followDaoSub;
    private final ExcelData excelData;
    /*****************************************************
     *  Modules
     ****************************************************/

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 회원 목록
     *
     * @param searchDto
     * @return 회원 리스트
     */
    public List<FollowDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = followDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<FollowDto> list = followDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 회원 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {

        //검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // get Lists
        List<FollowDto> followList = followDaoSub.getList(searchDto);

        if (!followList.isEmpty()) {
            // 문자변환
            stateText(followList);
        }

        // 엑셀 데이터 생성
        return excelData.createExcelData(followList, FollowDto.class);
    }

    /**
     * 회원 팔로우/팔로워 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getMemberFollowList(SearchDto searchDto) {

        // 목록 전체 count
        int totalCount = followDaoSub.getMemberFollowTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 팔로우/팔로워 리스트 공통 사용
        return followDaoSub.getMemberFollowList(searchDto);
    }

    /**
     * 회원 팔로잉 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    public List<FollowDto> getFollowingListByMemberUuid(FollowDto followDto) {
        List<FollowDto> list = followDaoSub.getFollowingList(followDto);
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }
        return list;
    }

    /**
     * 회원 팔로워 리스트 by memberUuid
     *
     * @param followDto memberUuid
     * @return list
     */
    public List<FollowDto> getFollowerListByMemberUuid(FollowDto followDto) {
        List<FollowDto> list = followDaoSub.getFollowerList(followDto);
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }
        return list;
    }

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

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
    public void stateText(List<FollowDto> list) {
        for (FollowDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(FollowDto dto) {
        // 상태 (0 : 정상 1: 탈퇴신청)
        if (dto.getMemberState() == 1) { // 탈퇴 대기
            dto.setMemberStateText(super.langMessage("lang.follow.state.out.wait"));
            dto.setMemberStateBg("badge-danger");
        } else { // 정상
            dto.setMemberStateText(super.langMessage("lang.follow.state.normal"));
            dto.setMemberStateBg("badge-success");
        }

        // 상태 (0 : 정상 1: 탈퇴신청)
        if (dto.getFollowState() == 1) { // 탈퇴 대기
            dto.setFollowStateText(super.langMessage("lang.follow.state.out.wait"));
            dto.setFollowStateBg("badge-danger");
        } else { // 정상
            dto.setFollowStateText(super.langMessage("lang.follow.state.normal"));
            dto.setFollowStateBg("badge-success");
        }
    }
}
