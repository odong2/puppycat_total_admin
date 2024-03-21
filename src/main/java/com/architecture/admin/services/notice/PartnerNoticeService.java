package com.architecture.admin.services.notice;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.notice.PartnerNoticeDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.notice.PartnerNoticeDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*****************************************************
 * 공지사항 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional(value = "dbcommonmaintransactionManager")
public class PartnerNoticeService extends BaseService {
    private final PartnerNoticeDaoSub partnerNoticeDaoSub;
    private final S3Library s3Library;
    private final ThumborLibrary thumborLibrary;

    // 설정된 S3 tmpUpload 폴더
    @Value("${cloud.aws.s3.tmpFolder}")
    private String s3TmpUploadFolder;
    /*****************************************************
     *  Modules
     ****************************************************/

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 공지사항 목록
     *
     * @param searchDto
     * @return 공지사항 리스트
     */
    public List<PartnerNoticeDto> getList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = partnerNoticeDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<PartnerNoticeDto> list = partnerNoticeDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 공지사항 메뉴 목록
     *
     * @param partnerNoticeDto
     * @return 공지사항 메뉴 리스트
     */
    public List<PartnerNoticeDto> getMenuList(PartnerNoticeDto partnerNoticeDto) {
        // 공지사항 메뉴 리스트
        return partnerNoticeDaoSub.getMenuList(partnerNoticeDto);
    }

    /**
     * 공지사항 상세
     *
     * @param idx
     * @return
     */
    public PartnerNoticeDto getViewNotice(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.NOTICE_IDX_ERROR);
        }
        // 회원 상세
        PartnerNoticeDto partnerNoticeDto = partnerNoticeDaoSub.getViewNotice(idx);
        // 이미지 리사이징
        String contents = thumborLibrary.replaceImgUrlToCFurl(partnerNoticeDto.getContents(), 200, "shop");
        partnerNoticeDto.setContents(contents);

        return partnerNoticeDto;
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
    public void stateText(List<PartnerNoticeDto> list) {
        for (PartnerNoticeDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PartnerNoticeDto dto) {
        // 상태 (0 : 삭제 1: 정상)
        if (dto.getState() == 1) {  // 정상
            dto.setStateText(super.langMessage("lang.notice.state.normal"));
            dto.setStateBg("badge-success");
        } else {
            dto.setStateText(super.langMessage("lang.notice.state.delete"));
            dto.setStateBg("badge-danger");
        }

        // 상태 (1: 상단 공지)
        if (dto.getIsTop() == 1) {
            dto.setIsTopText(super.langMessage("lang.notice.isTop"));
            dto.setIsTopBg("badge-success");
        } else {
            dto.setIsTopText(super.langMessage(""));
            dto.setIsTopBg("");
        }
    }
}
