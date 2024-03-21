package com.architecture.admin.services.member;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.excel.ExcelData;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.member.MemberPointDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.member.MemberPointDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberPointService extends BaseService {

    private final MemberPointDaoSub pointDaoSub;
    private final ExcelData excelData;

    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 회원 포인트 조회
     *
     * @param memberUuid : 회원 uuid
     * @return : MemberPointDto
     */
    public MemberPointDto getMemberPoint(String memberUuid) {
        MemberPointDto pointDto = pointDaoSub.getMemberPoint(memberUuid);

        if (ObjectUtils.isEmpty(pointDto)) {
            pointDto = new MemberPointDto();
        } else if (!ObjectUtils.isEmpty(pointDto)) {
            pointDto.setPointText(String.format("%,d", pointDto.getPoint()));
            pointDto.setUsePointText(String.format("%,d", pointDto.getUsePoint()));
            pointDto.setSavePointText(String.format("%,d", pointDto.getSavePoint()));
            pointDto.setExpirePointText(String.format("%,d", pointDto.getExpirePoint()));
        }

        return pointDto;
    }

    /**
     * 상품 주문 번호 체크
     *
     * @param memberPointDto : productOrderId, memberUuid
     */
    public void checkProductOrderId(MemberPointDto memberPointDto) {

        int result = pointDaoSub.checkProductOrderId(memberPointDto);

        if (result == 0) {
            throw new CustomException(CustomError.POINT_PRODUCT_ORDER_ID_ERROR); // 존재하지 않는 주문번호입니다.
        }
    }

    /**
     * 포인트 적립 or 사용 리스트
     *
     * @param searchDto : type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return
     */
    public List<MemberPointDto> getPointList(SearchDto searchDto) {

        // 날짜 유효성 검사
        dateLibrary.dateValidator(searchDto);
        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        // 목록 전체 count
        int totalCount = pointDaoSub.getPointTotalCnt(searchDto);
        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        List<MemberPointDto> pointList = new ArrayList<>();

        if (totalCount > 0) {
            pointList = pointDaoSub.getPointList(searchDto);

            for (MemberPointDto memberPointDto : pointList) {
                if (memberPointDto.getPoint() != null) {
                    memberPointDto.setPointText(String.format("%,d", memberPointDto.getPoint()));
                    memberPointDto.setRestPointText(String.format("%,d", memberPointDto.getRestPoint()));
                }
            }
        }

        return pointList;
    }

    /**
     * 엑셀 다운로드
     *
     * @param searchDto : type, searchWord, searchType, searchStartDate, searchEndDate, searchDateType
     * @return
     */
    public Map<String, Object> excelDownload(SearchDto searchDto) {
        // 날짜 유효성 검사
        dateLibrary.dateValidator(searchDto);
        // 검색어 공백제거
        searchDto.setSearchWord(searchDto.getSearchWord().trim());

        List<MemberPointDto> pointList = pointDaoSub.getPointList(searchDto);

        for (MemberPointDto memberPointDto : pointList) {
            if (memberPointDto.getPoint() != null) {
                memberPointDto.setPointText(String.format("%,d", memberPointDto.getPoint()));
                memberPointDto.setRestPointText(String.format("%,d", memberPointDto.getRestPoint()));
            }
        }

        return excelData.createExcelData(pointList, MemberPointDto.class);
    }

}
