package com.architecture.admin.services.qna;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.qna.PartnerQnaDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.qna.PartnerQnaDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*****************************************************
 * 1:1문의 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class PartnerQnaService extends BaseService {

    private final PartnerQnaDaoSub partnerQnaSub;
    private final ThumborLibrary thumborLibrary;
    /*****************************************************
     *  Modules
     ****************************************************/
    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 메뉴 리스트
     *
     * @return
     */
    public List<PartnerQnaDto> getMenuList() {
        // 1:1문의 메뉴 리스트
        return partnerQnaSub.getMenuList();
    }

    /**
     * 매크로 타이틀 리스트
     *
     * @return
     */
    public List<PartnerQnaDto> getMacroTitleList() {
        // 1:1문의 매크로 타이틀 리스트
        return partnerQnaSub.getMacroTitleList();
    }

    /**
     * 매크로 내용 조회
     *
     * @param idx
     * @return
     */
    public String getMacroContents(Integer idx) {
        return partnerQnaSub.getMacroContents(idx);
    }

    /**
     * 1:1 문의 가져오기
     *
     * @param searchDto searchWord searchType
     * @return 1:1 문의 리스트
     */
    public List<PartnerQnaDto> getListPartnerQna(SearchDto searchDto) {

        // 목록 전체 count
        int totalCount = partnerQnaSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 1:1문의 리스트
        List<PartnerQnaDto> list = partnerQnaSub.getListQna(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 1:1문의 상세
     *
     * @param idx
     * @return
     */
    public PartnerQnaDto getViewPartnerQna(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.QNA_IDX_ERROR);
        }

        // 1:1문의 상세
        PartnerQnaDto qnaDto = partnerQnaSub.getViewQna(idx);

        if (qnaDto == null) {
            // 존재하지 않는 문의글 입니다.
            throw new CustomException(CustomError.QNA_IDX_ERROR);
        }

        // 이미지 리사이징
        String contents = thumborLibrary.replaceImgUrlToCFurl(qnaDto.getContents(), 300, "shop");
        qnaDto.setContents(contents);

        return qnaDto;
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
    public void stateText(List<PartnerQnaDto> list) {
        for (PartnerQnaDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PartnerQnaDto dto) {
        // 상태 (1 : 대기중 2:처리중 3:답변완료 )
        if (dto.getAnswerState() == 1) {  // 대기중
            dto.setAnswerStateText(super.langMessage("lang.qna.answer.state.stay"));
            dto.setAnswerStateBg("badge-warning");
        } else if (dto.getAnswerState() == 2) {
            dto.setAnswerStateText(super.langMessage("lang.qna.answer.state.prc"));
            dto.setAnswerStateBg("badge-info");
        } else {
            dto.setAnswerStateText(super.langMessage("lang.qna.answer.state.ok"));
            dto.setAnswerStateBg("badge-success");
        }
        // 상태 (0:삭제 , 1:정상 )
        if (dto.getShow() == 0) {
            dto.setStateText(super.langMessage("lang.qna.state.delete"));
            dto.setStateBg("badge-danger");
        } else {
            dto.setStateText(super.langMessage("lang.qna.state.normal"));
            dto.setStateBg("badge-success");
        }
    }
}
