package com.architecture.admin.services.policy;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.commondao.policy.PolicyDao;
import com.architecture.admin.models.daosub.policy.PolicyDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.policy.PolicyDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*****************************************************
 * 이용약관 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional(value = "dbcommonmaintransactionManager")
@Slf4j
public class PolicyService extends BaseService {
    private final PolicyDaoSub policyDaoSub;
    private final PolicyDao policyDao;
    private final S3Library s3Library;
    private final AdminActionLogService adminActionLogService;   // 관리자 action log 처리용
    private final ThumborLibrary thumborLibrary;

    // 설정된 S3 tmpUpload 폴더
    @Value("${cloud.aws.s3.tmpFolder}")
    private String s3TmpUploadFolder;
    @Value("${env.server}")
    private String SERVER;
    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * 이용약관 등록
     *
     * @param
     * @return insertedIdx
     */
    public Integer registPolicy(PolicyDto policyDto) {

        // validate
        // 제목
        if (policyDto.getTitle() == null || policyDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.POL_TITLE_NULL);
        }
        // 상태
        if (policyDto.getState() == null || policyDto.getState() < 0) {
            // 상태값을 입력해주세요
            throw new CustomException(CustomError.POL_STATE_NULL);
        }
        // 필수여부
        if (policyDto.getRequired() == null || policyDto.getRequired().equals("")) {
            // 필수여부를 입력해주세요.
            throw new CustomException(CustomError.POL_REQUIRED_NULL);
        }
        if (policyDto.getMenuIdx() == null || policyDto.getMenuIdx() < 1) {
            // 카테고리를 선택해주세요.
            throw new CustomException(CustomError.POL_CATEGORY_NULL);
        }
        if (policyDto.getDetail() == null || policyDto.getDetail().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.POL_CONTENTS_NULL);
        }

        policyDao.updatePolicyCurrent(policyDto);

        // 이용약관 등록
        Integer iInsertIdx = insertPolicy(policyDto);
        // 이용약관 제목 등록
        insertPolicyTitle(iInsertIdx, policyDto);
        // 임시 폴더
        String tmpPath = SERVER + "/" + s3TmpUploadFolder;
        // s3에 저장될 path
        String s3Path = "policy/" + iInsertIdx;
        // 이미지 s3 upload, detail src 변경
        Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
        Matcher matcher = imgSrcPattern.matcher(policyDto.getDetail());

        while (matcher.find()) {
            // img src 주소
            String imgSrc = matcher.group(2).trim();

            // 임시 폴더에 저장된 file name
            String fileName = s3Library.getUploadedFileName(imgSrc, "common");
            // 파일 카피
            s3Library.copyFile(fileName, tmpPath, s3Path);
            // s3에 저장된 full url
            String s3FullUrl = s3Library.getUploadedFullUrl("/" + s3Path + "/" + fileName, "common");

            // 이미지 경로 변경
            policyDto.setDetail(policyDto.getDetail().replace(imgSrc, s3FullUrl));
        }

        // 이용약관 내용 등록
        insertPolicyDetail(iInsertIdx, policyDto);

        // 관리자 action log
        adminActionLogService.regist(policyDto, Thread.currentThread().getStackTrace());

        return iInsertIdx;
    }

    /**
     * 이용약관 수정
     *
     * @param policyDto idx title menuIdx state
     * @return
     */
    public Integer modifyPolicy(PolicyDto policyDto) {
        // validate
        // 제목
        if (policyDto.getTitle() == null || policyDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.POL_TITLE_NULL);
        }
        // 상태
        if (policyDto.getState() == null || policyDto.getState() < 0) {
            // 상태값을 입력해주세요
            throw new CustomException(CustomError.POL_STATE_NULL);
        }
        // 필수여부
        if (policyDto.getRequired() == null || policyDto.getRequired().equals("")) {
            // 필수여부를 입력해주세요.
            throw new CustomException(CustomError.POL_REQUIRED_NULL);
        }
        // 현행
        if (policyDto.getCurrent() == null || policyDto.getCurrent() < 0) {
            // 필수여부를 입력해주세요.
            throw new CustomException(CustomError.POL_CURRENT_NULL);
        }
        // 카테고리
        if (policyDto.getMenuIdx() == null || policyDto.getMenuIdx() < 1) {
            // 카테고리를 선택해주세요.
            throw new CustomException(CustomError.POL_CATEGORY_NULL);
        }
        if (policyDto.getDetail() == null || policyDto.getDetail().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.POL_CONTENTS_NULL);
        }

        // 현행 중복 체크
        if (policyDto.getCurrent() == 1) {
            // 같은 카테고리 내에 현행되고 있는 이용약관이 있는지 체크
            Boolean checkCurrent = checkDupleCurrent(policyDto);
            if (Boolean.TRUE.equals(checkCurrent)) {
                // 같은 카테고리 내 현행중인 약관이 존재합니다.
                throw new CustomException(CustomError.POL_CURRENT_DUPLE);
            }
        }

        // 이용약관 수정
        Integer iResult = updatePolicy(policyDto);
        policyDto.setPolicyIdx(policyDto.getIdx());
        if (iResult > 0) {
            // 이용약관 제목 수정
            updatePolicyTitle(policyDto);
            // 임시 폴더
            String tmpPath = SERVER + "/" + s3TmpUploadFolder;
            // s3에 저장될 path
            String s3Path = "policy/" + policyDto.getIdx();
            // 이미지 s3 upload, detail src 변경
            Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
            Matcher matcher = imgSrcPattern.matcher(policyDto.getDetail());

            while (matcher.find()) {
                // img src 주소
                String imgSrc = matcher.group(2).trim();

                // 임시 폴더에 저장된 이미지면 변환
                if (imgSrc.contains(tmpPath)) {
                    // 임시 폴더에 저장된 file name
                    String fileName = s3Library.getUploadedFileName(imgSrc, "common");
                    // 파일 카피
                    s3Library.copyFile(fileName, tmpPath, s3Path);
                    // s3에 저장된 full url
                    String s3FullUrl = s3Library.getUploadedFullUrl("/" + s3Path + "/" + fileName, "common");

                    // 이미지 경로 변경
                    policyDto.setDetail(policyDto.getDetail().replace(imgSrc, s3FullUrl));
                }
            }
            // 이용약관 내용 수정
            updatePolicyContents(policyDto);
        }

        // 관리자 action log
        adminActionLogService.regist(policyDto, Thread.currentThread().getStackTrace());
        return iResult;
    }

    /**
     * 카테고리 리스트
     *
     * @return
     */
    public List<PolicyDto> getPolicyMenuList(PolicyDto policyDto) {
        return policyDaoSub.getPolicyMenuList(policyDto);
    }

    /**
     * s3 s3TmpUploadFolder(data/tmpUpload)에 임시 이미지 저장
     *
     * @param uploadFile
     * @return
     */
    public String tempImage(List<MultipartFile> uploadFile) {
        // s3 이미지 등록
        List<HashMap<String, Object>> response = s3Library.uploadFileNew(uploadFile, s3TmpUploadFolder);

        return s3Library.getUploadedFullUrl(response.get(0).get("fileUrl").toString(), "common");
    }


    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    /**
     * 이용약관 목록
     *
     * @param searchDto
     * @return 이용약관 리스트
     */
    public List<PolicyDto> getPolicyList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = policyDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<PolicyDto> list = policyDaoSub.getPolicyList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 이용약관 상세
     *
     * @param idx
     * @return
     */
    public PolicyDto getViewPolicy(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.FAQ_IDX_ERROR);
        }
        PolicyDto policyDto = policyDaoSub.getViewPolicy(idx);

        // 이미지 리사이징
        String contents = thumborLibrary.replaceImgUrlToCFurl(policyDto.getDetail(), 200, "common");
        policyDto.setDetail(contents);
        // 이용약관 상세
        return policyDto;
    }

    /**
     * 카테고리 내 현행약관이 있는지
     *
     * @param policyDto
     * @return
     */
    public Boolean checkDupleCurrent(PolicyDto policyDto) {
        int iCount = policyDaoSub.getCountCurrent(policyDto);

        return iCount > 0;
    }

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/
    /**
     * 이용약관 등록
     *
     * @param policyDto menuIdx title
     * @return insertedIdx
     */
    public Integer insertPolicy(PolicyDto policyDto) {
        policyDto.setRegDate(dateLibrary.getDatetime());

        policyDao.insertPolicy(policyDto);
        return policyDto.getInsertedIdx();
    }

    /**
     * 이용약관 제목 등록
     *
     * @param insertIdx policyIdx
     * @param policyDto title
     */
    public void insertPolicyTitle(Integer insertIdx, PolicyDto policyDto) {
        policyDto.setPolicyIdx(insertIdx);
        policyDto.setRegDate(dateLibrary.getDatetime());

        policyDao.insertPolicyTitle(policyDto);
    }

    /**
     * 이용약관 내용 등록
     *
     * @param insertIdx policyIdx
     * @param policyDto contents
     */
    public void insertPolicyDetail(Integer insertIdx, PolicyDto policyDto) {
        policyDto.setPolicyIdx(insertIdx);
        policyDto.setRegDate(dateLibrary.getDatetime());

        policyDao.insertPolicyDetail(policyDto);
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/
    /**
     * 이용약관 수정
     *
     * @param policyDto
     * @return affectedRow
     */
    public Integer updatePolicy(PolicyDto policyDto) {

        return policyDao.updatePolicy(policyDto);
    }

    /**
     * 이용약관 제목 수정
     *
     * @param policyDto
     */
    public void updatePolicyTitle(PolicyDto policyDto) {

        policyDao.updatePolicyTitle(policyDto);
    }

    /**
     * 이용약관 내용 수정
     *
     * @param policyDto
     */
    public void updatePolicyContents(PolicyDto policyDto) {

        policyDao.updatePolicyDetail(policyDto);
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
    public void stateText(List<PolicyDto> list) {
        for (PolicyDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(PolicyDto dto) {
        // 상태 (0 : 삭제 1: 정상)
        if (dto.getState() == 1) {  // 정상
            dto.setStateText(super.langMessage("lang.policy.state.normal"));
            dto.setStateBg("badge-success");
        } else {
            dto.setStateText(super.langMessage("lang.policy.state.delete"));
            dto.setStateBg("badge-danger");
        }
        if (dto.getCurrent() != null && dto.getCurrent() == 1) {  // 정상
            dto.setCurrentText("Y");
        } else {
            dto.setCurrentText("N");
        }
    }
}
