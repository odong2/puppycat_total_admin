package com.architecture.admin.services.contents;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.models.daosub.contents.ContentsDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.contents.ContentsDto;
import com.architecture.admin.models.dto.contents.ContentsImgDto;
import com.architecture.admin.models.dto.contents.ContentsImgTagDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentsService extends BaseService {

    private final ContentsDaoSub contentsDaoSub;
    private final ThumborLibrary thumborLibrary;

    /*****************************************************
     *  Modules
     ****************************************************/

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 콘텐츠 메뉴 목록
     *
     * @return 콘텐츠 메뉴 리스트
     */
    public List<ContentsDto> getMenuList(ContentsDto contentsDto) {
        return contentsDaoSub.getMenuList(contentsDto);
    }

    /**
     * 회원 컨텐츠 최근 20개 리스트 by memberUuid
     *
     * @param contentsDto memberUuid
     * @return list
     */
    public List<ContentsDto> getContentLastTwentyCasesListByMemberUuid(ContentsDto contentsDto) {
        List<ContentsDto> list = contentsDaoSub.getContentLastTwentyCasesList(contentsDto);

        for (ContentsDto dto : list) {
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
     * 닉네임으로 memberUuid 검색
     *
     * @param nick 닉네임
     * @return memberUuid
     */
    public String searchMember(String nick) {

        return memberDaoSub.getMemberUuidByNick(nick);

    }

    /**
     * 콘텐츠 목록
     *
     * @param searchDto
     * @return 콘텐츠 리스트
     */
    public List<ContentsDto> getList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = contentsDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 콘텐츠 리스트
        List<ContentsDto> list = contentsDaoSub.getList(searchDto);

        for (ContentsDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);

            // 이미지 사이즈 변환
            String fullUrl = thumborLibrary.setCFurl(dto.getImgWidth(), dto.getImgHeight(), dto.getThumbnailUrl(), 50, "sns");
            dto.setThumbnailUrl(fullUrl);
        }

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 급상승 인기 콘텐츠 리스트
     *
     * @param searchDto
     * @return
     */
    public List<ContentsDto> getHourPopularList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 급상승 인기 콘텐츠 카운트
        Integer totalCount = contentsDaoSub.getHourPopularTotalCnt(searchDto);
        totalCount = ObjectUtils.isEmpty(totalCount) ? 0 : totalCount;
        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 급상승 인기 콘텐츠 리스트
        List<ContentsDto> list = contentsDaoSub.getHourPopularList(searchDto);

        for (ContentsDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);

            // 이미지 사이즈 변환
            String fullUrl = thumborLibrary.setCFurl(dto.getImgWidth(), dto.getImgHeight(), dto.getThumbnailUrl(), 50, "sns");
            dto.setThumbnailUrl(fullUrl);
        }

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 주간 인기 콘텐츠 리스트
     *
     * @param searchDto
     * @return
     */
    public List<ContentsDto> getWeekPopularList(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 급상승 인기 콘텐츠 카운트
        Integer totalCount = contentsDaoSub.getWeekPopularTotalCnt(searchDto);
        totalCount = ObjectUtils.isEmpty(totalCount) ? 0 : totalCount;
        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 급상승 인기 콘텐츠 리스트
        List<ContentsDto> list = contentsDaoSub.getWeekPopularList(searchDto);

        for (ContentsDto dto : list) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);

            // 이미지 사이즈 변환
            String fullUrl = thumborLibrary.setCFurl(dto.getImgWidth(), dto.getImgHeight(), dto.getThumbnailUrl(), 50, "sns");
            dto.setThumbnailUrl(fullUrl);
        }

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 콘텐츠 상세
     *
     * @param contentsDto 콘텐츠 idx
     * @return 콘텐츠 상세 리스트
     */
    public List<ContentsDto> getDetailList(ContentsDto contentsDto) {
        // 콘텐츠 상세
        List<ContentsDto> list = contentsDaoSub.getDetailList(contentsDto);

        // 신고된 콘텐츠 상세라면 신고 당시 내용으로 세팅
        if (!ObjectUtils.isEmpty(list.get(0).getReportContents())) {
            list.get(0).setContents(list.get(0).getReportContents());
        }

        String contents;

        for (ContentsDto dto : list) {
            // 멘션 복호화
            contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);
        }

        return list;
    }

    /**
     * 콘텐츠 이미지 상세
     *
     * @param contentsDto 콘텐츠 idx
     * @return 콘텐츠 이미지 상세 리스트
     */
    public List<ContentsImgDto> getDetailImgList(ContentsDto contentsDto) {
        // 콘텐츠 이미지 상세
        List<ContentsImgDto> list = contentsDaoSub.getDetailImgList(contentsDto);

        for (ContentsImgDto dto : list) {
            // 이미지 회원태그 세팅
            if (dto.getContentsIdx() != null) {
                List<ContentsImgTagDto> getTagImgList = contentsDaoSub.getImgTag(dto);
                dto.setImgTagList(getTagImgList);
            }

            // 이미지 사이즈 변환
            String fullUrl = thumborLibrary.setCFurl(dto.getImgWidth(), dto.getImgHeight(), dto.getUrl(), 250, "sns");
            dto.setOriginUrl(dto.getUrl());
            dto.setUrl(fullUrl);
        }

        return list;
    }

    /**
     * 컨텐츠 리스트 조회 [id, nick, url]
     *
     * @param idxList : 컨텐츠 idxList
     * @return
     */
    public List<ContentsDto> getContentsList(List<Long> idxList) {
        // 컨텐츠 리스트
        List<ContentsDto> contentsList = new ArrayList<>();

        idxList.forEach(idx -> {
            ContentsDto contentsDto = contentsDaoSub.getListByIdx(idx); // 컨텐츠 정보 조회
            contentsList.add(contentsDto); // 컨텐츠 리스트 추가
        });

        // cfUrl setting
        if (!ObjectUtils.isEmpty(contentsList)) {
            for (ContentsDto dto : contentsList) {
                // 이미지 사이즈 변환
                String fullUrl = thumborLibrary.setCFurl(dto.getImgWidth(), dto.getImgHeight(), dto.getThumbnailUrl(), 50, "sns");
                dto.setThumbnailUrl(fullUrl);
            }
        }

        return contentsList;
    }


    /**
     * 이미지 확대
     *
     * @param imgDto
     * @return
     */
    public String getContentsBigImg(ContentsImgDto imgDto) {
        // 이미지 사이즈 변환
        return thumborLibrary.setCFurl(imgDto.getImgWidth(), imgDto.getImgHeight(), imgDto.getOriginUrl(), 1000, "sns");
    }


    /*****************************************************
     *  SubFunction - Insert
     ****************************************************/

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
    public void stateText(List<ContentsDto> list) {
        for (ContentsDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(ContentsDto dto) {
        // 상태 (0 : 삭제 1: 정상)
        if (dto.getContentsState() == 0) { // 삭제
            dto.setStateText(super.langMessage("lang.comment.state.delete"));
            dto.setStateBg("badge-danger");
        } else if(dto.getContentsState() == 1) { // 정상
            dto.setStateText(super.langMessage("lang.comment.state.normal"));
            dto.setStateBg("badge-success");
        } else {
            dto.setStateText(super.langMessage("lang.content.state.restrain"));
            dto.setStateBg("badge-warning");
        }
    }

    /**
     * 좋아요 한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getLikeMemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = contentsDaoSub.getLikeMemberTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        return contentsDaoSub.getLikeMemberList(searchDto);
    }

    /**
     * 저장한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getSaveMmemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = contentsDaoSub.getSaveMemberTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        return contentsDaoSub.getSaveMemberList(searchDto);
    }

    /**
     * 댓글 작성한 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getCommentMemberList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = contentsDaoSub.getCommentsTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        List<MemberDto> commentsList = contentsDaoSub.getCommentsList(searchDto);

        for (MemberDto dto : commentsList) {
            // 멘션 복호화
            String contents = super.mentionDecrypt(dto.getContents());
            // 태그 복호화
            contents = super.tagDecrypt(contents);

            dto.setContents(contents);
        }

        return commentsList;
    }

    /**
     * 컨텐츠 신고 회원 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getReportMemberList(SearchDto searchDto) {

        // 목록 전체 count
        int totalCount = contentsDaoSub.getReportTotalCnt(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        return contentsDaoSub.getReportMemberList(searchDto);
    }


    /*****************************************************
     *  Curl
     ****************************************************/

}
