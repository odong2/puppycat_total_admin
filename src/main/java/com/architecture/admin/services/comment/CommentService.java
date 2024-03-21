package com.architecture.admin.services.comment;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.comment.CommentDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.comment.CommentDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.contents.ContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService extends BaseService {

    private final CommentDaoSub commentDaoSub;
    private final ContentsService contentsService;

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 댓글 목록
     *
     * @param searchDto
     * @return 댓글 리스트
     */
    public List<CommentDto> getList(SearchDto searchDto){
        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = commentDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 댓글 리스트
        List<CommentDto> list = commentDaoSub.getList(searchDto);
        List<Long> idxList = new ArrayList<>();

        list.forEach(commentDto -> {
            idxList.add(commentDto.getIdx());
        });

        // 신고 수 setting
        if (!ObjectUtils.isEmpty(idxList)) {
            // idx, reportCnt
            List<CommentDto> reportList = commentDaoSub.getReportCntByIdxList(idxList);

            for (CommentDto commentDto : list) {
                Long idx = commentDto.getIdx();

                for (CommentDto reportDto : reportList) {
                    if (idx.equals(reportDto.getIdx())) {
                        commentDto.setReportCnt(reportDto.getReportCnt());
                    }
                }
            }
        }

        // 콘텐츠 리스트
        List<Long> contentsIdxList = new ArrayList<>();
        list.forEach(commentDto -> contentsIdxList.add(commentDto.getContentsIdx()));
        List<ContentsDto> contentsList = contentsService.getContentsList(contentsIdxList);

        for (CommentDto dto : list) {

            // 콘텐츠 세팅
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

            // 댓글/답글 구분값 세팅
            if (dto.getParentIdx() != null && dto.getParentIdx() == 0) { // 댓글(부모)
                dto.setDivision(super.langMessage("lang.comment.parent"));
            } else { // 답글(자식)
                dto.setDivision(super.langMessage("lang.comment.comment"));
            }

            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);
        } // end of for

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 회원 댓글 최근 20개 리스트 by memberUuid
     *
     * @param commentDto memberUuid
     * @return list
     */
    public List<CommentDto> getCommentLastTwentyCasesListByMemberUuid(CommentDto commentDto){
        List<CommentDto> list = commentDaoSub.getCommentLastTwentyCasesList(commentDto);

        for (CommentDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);
        }

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 회원 댓글 상세 by idx
     *
     * @param commentDto
     * @return CommentDto
     */
    public CommentDto getView(CommentDto commentDto) {
        if (commentDto.getIdx() == null || commentDto.getIdx() < 1) {
            throw new CustomException(CustomError.COMMENT_IDX_ERROR);
        }

        // 댓글 상세 정보
        CommentDto commentInfo = commentDaoSub.getDetailComment(commentDto);

        // 신고된 댓글 상세라면 신고 당시 내용으로 세팅
        if (!ObjectUtils.isEmpty(commentInfo.getReportContents())) {
            commentInfo.setContents(commentInfo.getReportContents());
        }

        // 멘션 복호화
        String contents = super.mentionDecrypt(commentInfo.getContents());
        // 태그 복호화
        contents = super.tagDecrypt(contents);
        // 댓글 내용 세팅
        commentInfo.setContents(contents);

        // 상세 정보
        return commentInfo;
    }

    /**
     * 좋아요 한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getLikeMemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = commentDaoSub.getLikeMemberTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        return commentDaoSub.getLikeMemberList(searchDto);
    }

    /**
     * 신고한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getReportMemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = commentDaoSub.getReportMemberTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        return commentDaoSub.getReportMemberList(searchDto);
    }

    /**
     * 댓글 작성한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getCommentMemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = commentDaoSub.getCommentsTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        List<MemberDto> commentsList = commentDaoSub.getCommentsList(searchDto);

        for (MemberDto dto : commentsList) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);
        }

        return commentsList;
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/

    /*****************************************************
     *  Common
     ****************************************************/
    /**
     * 문자변환 list
     *
     * @param list
     */
    public void stateText(List<CommentDto> list) {
        for (CommentDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(CommentDto dto) {
        // 상태 (0 : 노출 1: 미 노출)
        if (dto.getState() != null && dto.getState() == 0) { // 노출
            dto.setStateText(super.langMessage("lang.comment.state.delete"));
            dto.setStateBg("badge-danger");
        } else { // 정상
            dto.setStateText(super.langMessage("lang.comment.state.normal"));
            dto.setStateBg("badge-success");
        }
        if (dto.getContentsState() != null && dto.getContentsState() == 1) { // 정상
            dto.setContentsStateText(super.langMessage("lang.contents.state.normal"));
            dto.setContentsStateBg("badge-success");
        } else { // 삭제
            dto.setContentsStateText(super.langMessage("lang.contents.state.delete"));
            dto.setContentsStateBg("badge-danger");
        }
    }
}
