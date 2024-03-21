package com.architecture.admin.services.report;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.models.daosub.report.CommentReportDaoSub;
import com.architecture.admin.models.daosub.report.ReportDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.report.CommentReportDto;
import com.architecture.admin.models.dto.report.ReportCodeDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.contents.ContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentReportService extends BaseService {

    private final CommentReportDaoSub commentReportDaoSub;
    private final ReportDaoSub reportDaoSub;
    private final ContentsService contentsService;
    private final ExcelData excelData;

    @Value("${report.code.max}")
    private int codeMax;  // 신고사유 코드 MAX

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 댓글 신고 목록
     *
     * @param searchDto 검색조건
     * @return 댓글 신고 리스트
     */
    public List<CommentReportDto> getListReportComment(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = commentReportDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        List<CommentReportDto> list = commentReportDaoSub.getList(searchDto);

        // 컨텐츠 리스트
        List<Long> contentsIdxList = new ArrayList<>();
        list.forEach(commentReportDto -> contentsIdxList.add(commentReportDto.getContentsIdx()));

        List<ContentsDto> contentsList = contentsService.getContentsList(contentsIdxList);

        for (CommentReportDto dto : list) {
            for (int index = 0; index < contentsList.size(); index++) {
                ContentsDto contentsDto = contentsList.get(index);
                Long contentsIdx = contentsDto.getIdx();

                if (contentsIdx.equals(dto.getContentsIdx())) {
                    dto.setContentsMemberNick(contentsDto.getMemberNick());     // 컨텐츠 작성자 닉네임
                    dto.setContentsThumbnailUrl(contentsDto.getThumbnailUrl()); // 컨텐츠 이미지 URL
                    dto.setContentsRegDate(contentsDto.getRegDate());           // 컨텐츠 등록일
                    dto.setContentsState(contentsDto.getContentsState());       // 컨텐츠 상태
                    contentsList.remove(index);
                    break;
                }
            }

            // 댓글/답글 구분값 & 부모 댓글 내용 세팅
            if (dto.getParentIdx() != null && dto.getParentIdx() == 0) { // 댓글(부모)
                dto.setDivision(super.langMessage("lang.comment.parent"));
                dto.setParentContents(" ");
            } else { // 답글(자식)
                dto.setDivision(super.langMessage("lang.comment.comment"));
                CommentReportDto parentComment = commentReportDaoSub.getParentCommentByIdx(dto.getParentIdx());

                // 멘션 복호화
                String parentContents = super.mentionDecrypt(parentComment.getParentContents());
                // 태그 복호화
                parentContents = super.tagDecrypt(parentContents);

                dto.setParentContents(parentContents);
            }

            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);

        } // end of for


        // 문자변환
        if (!list.isEmpty()) {
            stateText(list);
        }

        return list;
    }

    /**
     * 댓글 신고 목록
     *
     * @param searchDto 검색조건, pagination
     * @return 신고 목록 list
     */
    public List<CommentReportDto> getList(SearchDto searchDto) {
        List<CommentReportDto> list = commentReportDaoSub.getList(searchDto);
        for (CommentReportDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);
            dto.setContents(contents);
        }

        return list;
    }

    /**
     * 신고사유 목록
     *
     * @return 신고사유 list
     */
    public List<ReportCodeDto> getListReportCode() {
        return reportDaoSub.getListReportCode();
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) throws ParseException {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // list
        List<CommentReportDto> list = commentReportDaoSub.getList(searchDto);

        for (CommentReportDto dto : list) {

            // 댓글/답글 구분값 & 부모 댓글 내용 세팅
            if (dto.getParentIdx() != null && dto.getParentIdx() == 0) { // 댓글(부모)
                dto.setDivision(super.langMessage("lang.comment.parent"));
                dto.setParentContents(" ");
            } else { // 답글(자식)
                dto.setDivision(super.langMessage("lang.comment.comment"));
                CommentReportDto parentComment = commentReportDaoSub.getParentCommentByIdx(dto.getParentIdx());

                // 멘션 복호화
                String parentContents = super.mentionDecrypt(parentComment.getParentContents());
                // 태그 복호화
                parentContents = super.tagDecrypt(parentContents);

                dto.setParentContents(parentContents);
            }

            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);

        } // end of for

        // 상태값 문자 변환
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        // 엑셀 데이터 변환
        return excelData.createExcelData(list, CommentReportDto.class);
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  SubFunction - Delete
     ****************************************************/

    /*****************************************************
     *  SubFunction - Etc
     ****************************************************/
    /**
     * 문자변환 list
     */
    public void stateText(List<CommentReportDto> list) {

        for (CommentReportDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     */
    public void stateText(CommentReportDto dto) {

        // 신고 상태
        if (dto.getState() != null) {
            if (dto.getState() == 0) { // 신고 취소
                dto.setStateText(super.langMessage("lang.report.state.user.delete"));
                dto.setStateBg("badge-success");
            } else if (dto.getState() == 1) { // 정상(신고상태)
                dto.setStateText(super.langMessage("lang.report.state.normal"));
                dto.setStateBg("badge-danger");
            }
        }
        // 신고사유
        if (dto.getReportCodeIdx() != codeMax) {
            dto.setReason(" ");
        }
        // 콘텐츠 상태
        if (dto.getContentsState() != null && dto.getContentsState() == 1) { // 정상
            dto.setContentsStateText(super.langMessage("lang.report.contents.state.normal"));
            dto.setContentsStateBg("badge-success");
        } else { // 삭제
            dto.setContentsStateText(super.langMessage("lang.report.contents.state.delete"));
            dto.setContentsStateBg("badge-danger");
        }
        // 댓글 상태
        if (dto.getCommentState() != null && dto.getCommentState() == 1) { // 정상
            dto.setCommentStateText(super.langMessage("lang.report.contents.state.normal"));
        } else { // 삭제
            dto.setCommentStateText(super.langMessage("lang.report.contents.state.delete"));
        }
        // 관리자 확인 상태 세팅
        if (ObjectUtils.isEmpty(dto.getAdminChkState()) || (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 0)) { // 미확인
            // 미확인
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.require"));
            dto.setAdminChkBg("badge-danger");
        } else if (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 1) {
            // 확인완료
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.normal"));
            dto.setAdminChkBg("badge-success");
        } else if (!ObjectUtils.isEmpty(dto.getAdminChkState()) && dto.getAdminChkState() == 9) {
            // 제재
            dto.setAdminChkStateText(super.langMessage("lang.report.state.admin.check.state.restrain"));
            dto.setAdminChkBg("badge-warning");
        }
    }

}
