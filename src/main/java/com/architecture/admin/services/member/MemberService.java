package com.architecture.admin.services.member;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.member.MemberDao;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.models.dto.member.MemberImageDto;
import com.architecture.admin.models.dto.shopping.brand.BrandDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*****************************************************
 * 회원 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService extends BaseService {
    private final MemberDao memberDao;
    private final ThumborLibrary thumborLibrary;
    private final ExcelData excelData;
    @Value("${member.badge.cnt}")
    private Integer badgeCnt;

    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 회원 엑셀 다운로드
     *
     * @param searchDto
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // get Lists
        List<MemberDto> memberExcelData = memberDaoSub.getListMember(searchDto);

        // 상태값 문자 변환
        stateText(memberExcelData);

        // 엑셀 데이터 생성
        return excelData.createExcelData(memberExcelData, MemberDto.class);
    }

    /**
     * 회원 uuid 조회
     *
     * @param searchDto : searchType, searchWord
     * @return
     */
    public List<String> getMemberUuid(SearchDto searchDto) {

        return memberDaoSub.getUuidList(searchDto);
    }

    /**
     * 회원 정보 조회 by uuid
     *
     * @param memberUuidList uuid
     * @return
     */
    public List<MemberDto> getMemberInfoByUuid(List<String> memberUuidList) {
        return memberDaoSub.getMemberInfoListByUuid(memberUuidList);
    }

    /**
     * 정상 회원 리스트 체크
     *
     * @param uuidList
     * @return
     */
    public boolean checkMemberUuidList(List<String> uuidList) {
        boolean result = true;

        if (ObjectUtils.isEmpty(uuidList)) {
            throw new CustomException(CustomError.MEMBER_UUID_EMPTY); // 회원 uuid가 비었습니다.
        }

        MemberDto memberDto = new MemberDto();

        for (String uuid : uuidList) {
            memberDto.setUuid(uuid);
            Boolean isExist = getCountByUuid(memberDto); // 정상 회원이면 true

            if (isExist == false) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 회원 유효성 검사 by puppycat_member.idx
     *
     * @param memberDto idx
     * @return true : 유효
     */
    public Boolean getCountByUuid(MemberDto memberDto) {
        int iCount = memberDaoSub.getCountByUuid(memberDto.getUuid());

        return iCount > 0;
    }

    /**
     * 닉네임 유효성 확인
     *
     * @param memberDto nick
     * @return true
     */
    @SneakyThrows
    public Boolean checkNick(MemberDto memberDto) {
        // 닉네임 검증
        String nick = memberDto.getNick();

        if (nick == null || nick.equals("")) {
            throw new CustomException(CustomError.NICK_EMPTY);
        }

        //닉네임 길이 검사
        if (nick.length() < 2 || nick.length() > 20) {
            throw new CustomException(CustomError.NICK_LENGTH_ERROR);
        }

        // 숫자/한글/영어/언더바/온점만 입력 가능
        if (!Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣_]*$", nick)) {
            throw new CustomException(CustomError.NICK_STRING_ERROR);
        }

        // 외계어 검사
        String nickCheck = new String(nick.getBytes("euc-kr"), "euc-kr");
        if (!nick.equals(nickCheck)) {
            throw new CustomException(CustomError.NICK_STRING_ERROR);
        }

        // 닉네임 중복체크
        Boolean bDupleNick = checkDupleNick(memberDto);
        if (Boolean.TRUE.equals(bDupleNick)) {
            throw new CustomException(CustomError.NICK_DUPLE);
        }
        return true;
    }

    /**
     * 쿠폰 적용 회원 검색
     *
     * @param searchDto searchWord searchType
     * @return list
     */
    public List<MemberDto> searchMember(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = memberDaoSub.getMemberSearchCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // list
        return memberDaoSub.getMemberSearchList(searchDto);
    }

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 회원 목록
     *
     * @param searchDto searchType, searchWord, searchDateType, searchStartDate, searchEndDate, isDel
     * @return 회원 리스트
     */
    public List<MemberDto> getListMember(SearchDto searchDto) {

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = memberDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<MemberDto> listMember = memberDaoSub.getListMember(searchDto);

        if (!listMember.isEmpty()) {
            // 문자변환
            stateText(listMember);
        }
        return listMember;
    }

    /**
     * 회원 상세
     *
     * @param memberIdx 회원 uuid
     * @return MemberDto
     */
    public MemberDto getViewMember(Long memberIdx) {
        if (memberIdx == null || memberIdx <= 0) {
            throw new CustomException(CustomError.IDX_ERROR); // 회원 UUID가 유효하지 않습니다.
        }

        // 회원 상세
        MemberDto viewMember = memberDaoSub.getViewMember(memberIdx);

        if (ObjectUtils.isEmpty(viewMember)) {
            throw new CustomException(CustomError.MEMBER_NULL); // 존재하지 않는 회원입니다.
        }

        // 프로필 이미지 사이즈 변환
        if (!ObjectUtils.isEmpty(viewMember.getMemberImg())) {
            MemberImageDto memberImg = viewMember.getMemberImg();
            String fullUrl = thumborLibrary.setCFurl(memberImg.getImgWidth(), memberImg.getImgHeight(), memberImg.getUrl(), 150, "member");
            memberImg.setUrl(fullUrl);
        }

        // 문자변환
        stateText(viewMember);

        return viewMember;
    }

    /**
     * 팔로우/팔로워 랭킹 리스트
     *
     * @param searchDto
     * @return
     */
    public List<MemberDto> getRankList(SearchDto searchDto) {

        // 회원리스트
        List<MemberDto> memberList = memberDaoSub.getRankList(searchDto);

        if (!memberList.isEmpty()) {
            // 문자변환
            stateText(memberList);
        }

        return memberList;
    }

    /**
     * 최근 교류많은 회원 20명 리스트 by memberUuid
     *
     * @param memberDto memberUuid
     * @return list
     */
    public List<MemberDto> getInteractiveLastTwentyCasesListByMemberUuid(MemberDto memberDto) {
        List<MemberDto> list = memberDaoSub.getInteractiveLastTwentyCasesList(memberDto);
        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 회원 중복 닉네임 검사
     *
     * @param memberDto nick
     * @return count
     */
    public Boolean checkDupleNick(MemberDto memberDto) {
        int iCount = memberDaoSub.getCountByNick(memberDto);

        return iCount > 0;
    }

    /**
     * 회원 카운트 값 by idx
     *
     * @param memberDto idx
     * @return count
     */
    public Boolean getCountByIdx(MemberDto memberDto) {
        int iCount = memberDaoSub.getCountByIdx(memberDto);

        return iCount > 0;
    }

    /**
     * 회원 UUID 가져오기 by idx
     */
    public String getMemberUuidByIdx(Long memberIdx) {
        String memberUUid = memberDaoSub.getMemberUuidByIdx(memberIdx);

        if (memberUUid == null || memberUUid.equals("")) {
            throw new CustomException(CustomError.MEMBER_NULL); // 존재하지 않는 회원입니다.

        }
        return memberUUid;
    }

    /**
     * 회원 정보
     *
     * @param uuidList
     * @return
     */
    public List<MemberDto> getMemberInfo(List<String> uuidList) {
        List<MemberDto> memberList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(uuidList)) {
            memberList = memberDaoSub.getMemberInfoByUuidList(uuidList);
        }
        return memberList;
    }

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/
    /**
     * 닉네임 등록 수정 로그
     *
     * @param memberDto nick idx gender idx
     */
    public void insertNickLog(MemberDto memberDto) {
        memberDto.setRegDate(dateLibrary.getDatetime());
        memberDao.insertNickLog(memberDto);
    }
    /*****************************************************
     *  SubFunction - Update
     ****************************************************/
    /**
     * 닉네임 업데이트
     *
     * @param memberDto idx nick
     * @return affectedRow
     */
    public int updateNick(MemberDto memberDto) {
        if (memberDto.getIdx() == null || memberDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_MEMBER_ERROR);
        }

        return memberDao.updateNick(memberDto);
    }

    /**
     * 회원 정보 업데이트
     *
     * @param memberDto memberIdx phone birth lang gender
     * @return affectedRow
     */
    public int updateInfo(MemberDto memberDto) {
        if (memberDto.getIdx() == null || memberDto.getIdx() < 1) {
            throw new CustomException(CustomError.IDX_MEMBER_ERROR);
        }
        return memberDao.updateInfo(memberDto);
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
    public void stateText(List<MemberDto> list) {
        for (MemberDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(MemberDto dto) {
        // 상태값
        if (dto.getState() != null) {
            if (dto.getState() == 1) { //정상
                dto.setStateText(super.langMessage("lang.member.state.normal"));
                dto.setStateBg("bg-success");
            } else if (dto.getState() == 0) { // 탈퇴
                dto.setStateText(super.langMessage("lang.member.state.out"));
                dto.setStateBg("bg-danger");
            }
        }

        // 탈퇴 상태값
        if (dto.getIsDel() != null) {
            if (dto.getIsDel() == 0) {//정상
                dto.setIsDelText(super.langMessage("lang.member.state.normal"));
                dto.setIsDelBg("bg-success");
            } else if (dto.getIsDel() == 1) {// 탈퇴 대기
                dto.setIsDelText(super.langMessage("lang.member.state.out.wait"));
                dto.setIsDelBg("bg-danger");
            }
        }

        // 마켓팅 수신 상태값
        if (dto.getMarketingState() != null) {
            if (dto.getMarketingState() == 0) { // 거절
                dto.setMarketingStateText(super.langMessage("lang.policy.disagree"));
                dto.setMarketingStateBg("bg-danger");
            } else if (dto.getMarketingState() == 1) {// 동의
                dto.setMarketingStateText(super.langMessage("lang.policy.agree"));
                dto.setMarketingStateBg("bg-success");
            }
        }

        // 성별
        if (dto.getGender() != null) {
            if (dto.getGender().equals("M")) {// 남
                dto.setGenderText(super.langMessage("lang.member.gender.male"));
            } else if (dto.getGender().equals("F")) {// 여
                dto.setGenderText(super.langMessage("lang.member.gender.female"));
            } else {
                dto.setGenderText("");
            }
        }

        // 뱃지 세팅
        if (dto.getFollowerCnt() != null) {
            if (dto.getFollowerCnt() >= badgeCnt) {//  뱃지 Y
                dto.setBadge("Y");
            } else {
                dto.setBadge("N");
            }
        }

        // 언어 표기
        if (dto.getLang() != null) {
            switch (dto.getLang()) {
                case "ko" -> dto.setLangText(super.langMessage("lang.member.lang.ko"));
                case "en" -> dto.setLangText(super.langMessage("lang.member.lang.en"));
                default -> dto.setLangText(dto.getLang());
            }
        }

        // 간편로그인 설정
        if (dto.getSimpleType() == null) {
            dto.setSimpleType("");
        }
    }
}
