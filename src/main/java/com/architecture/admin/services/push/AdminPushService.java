package com.architecture.admin.services.push;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.models.daosub.push.AdminPushDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.push.AdminPushDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPushService extends BaseService {
    private final AdminPushDaoSub adminPushDaoSub;


    /*****************************************************
     *  Modules
     ****************************************************/

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 푸시 목록
     *
     * @param searchDto
     * @return 푸시 리스트
     */
    public List<AdminPushDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = adminPushDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 푸시 리스트
        List<AdminPushDto> list = adminPushDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * state 가 정상인 워커가 있는지 체크
     *
     * @return
     */
    public Boolean getWorkerCheck() {
        Integer iCount = adminPushDaoSub.getWorkerCheck();

        return iCount > 0;
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
    public void stateText(List<AdminPushDto> list) {
        for (AdminPushDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(AdminPushDto dto) {
        // 푸시 타입 (9 : 공지 10: 이벤트)
        if (dto.getTypeIdx() == 9) { // 공지
            dto.setTypeTitle(super.langMessage("lang.push.admin.type.notice"));
        } else { // 이벤트
            dto.setTypeTitle(super.langMessage("lang.push.admin.type.event"));
        }

        // 컨텐츠 이동 타입
        if (dto.getContentsType() == 1) { // 공지
            dto.setMoveLink("/notice/view/" + dto.getContentsIdx());
        } else { // 2. 컨텐츠
            dto.setMoveLink("/contents/view/" + dto.getContentsIdx());
        }
    }
}
