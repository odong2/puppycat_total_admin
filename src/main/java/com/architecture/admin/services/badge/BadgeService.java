package com.architecture.admin.services.badge;


import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.badge.BadgeDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.badge.BadgeDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*****************************************************
 * 팔로우 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService extends BaseService {
    private final BadgeDaoSub badgeDaoSub;
    @Value("${member.badge.cnt}")
    private Integer badgeCnt;
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
    public List<BadgeDto> getList(SearchDto searchDto){
        // 뱃지를 부여해 줄 팔로워 카운트 값 세팅
        searchDto.setCount(badgeCnt);
        // 목록 전체 count
        int totalCount = badgeDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<BadgeDto> list = badgeDaoSub.getList(searchDto);

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
    public void stateText(List<BadgeDto> list) {
        for (BadgeDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(BadgeDto dto) {
        // 상태 (0 : 정상 1: 탈퇴신청)
        if (dto.getMemberState() == 1) { // 탈퇴 대기
            dto.setMemberStateText(super.langMessage("lang.follow.state.out.wait"));
            dto.setMemberStateBg("badge-danger");
        }
        else{ // 정상
            dto.setMemberStateText(super.langMessage("lang.follow.state.normal"));
            dto.setMemberStateBg("badge-success");
        }

    }
}
