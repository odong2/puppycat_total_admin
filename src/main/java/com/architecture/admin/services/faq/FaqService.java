package com.architecture.admin.services.faq;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.commondao.faq.FaqDao;
import com.architecture.admin.models.daosub.faq.FaqDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.faq.FaqDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*****************************************************
 * 자주하는 질문 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional(value = "dbcommonmaintransactionManager")
public class FaqService extends BaseService {
    private final FaqDao faqDao;
    private final FaqDaoSub faqDaoSub;
    private final S3Library s3Library;
    private final AdminActionLogService adminActionLogService;
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
     * 자주하는 질문 등록
     *
     * @param faqDto menuIdx title adminId regdate
     * @return insertedIdx
     */
    public Integer registFaq(FaqDto faqDto) {

        // validate
        // 제목
        if (faqDto.getTitle() == null || faqDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.FAQ_TITLE_NULL);
        }
        // 공지 종류
        if (faqDto.getMenuIdx() == null || faqDto.getMenuIdx() < 0) {
            // 공지 종류를 입력해주세요
            throw new CustomException(CustomError.FAQ_MENU_NULL);
        }
        // 상태
        if (faqDto.getState() == null || faqDto.getState() < 0) {
            // 상태값을 입력해주세요
            throw new CustomException(CustomError.FAQ_STATE_NULL);
        }
        if (faqDto.getContents() == null || faqDto.getContents().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.FAQ_CONTENTS_NULL);
        }

        // 자주하는 질문 등록
        Integer iInsertIdx = insertFaq(faqDto);
        // 자주하는 질문 제목 등록
        insertFaqTitle(iInsertIdx, faqDto);
        // 임시 폴더
        String tmpPath = SERVER + "/" + s3TmpUploadFolder;
        // s3에 저장될 path
        String s3Path = "faq/" + iInsertIdx;
        // 이미지 s3 upload, detail src 변경
        Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
        Matcher matcher = imgSrcPattern.matcher(faqDto.getContents());

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
            faqDto.setContents(faqDto.getContents().replace(imgSrc, s3FullUrl));
        }

        // 자주하는 질문 내용 등록
        insertFaqContents(iInsertIdx, faqDto);

        // 관리자 action log
        adminActionLogService.regist(faqDto, Thread.currentThread().getStackTrace());

        return iInsertIdx;
    }

    /**
     * 자주하는 질문 수정
     *
     * @param faqDto idx title menuIdx state
     * @return
     */
    public Integer modifyFaq(FaqDto faqDto) {
        // validate
        if (faqDto.getIdx() == null || faqDto.getIdx() < 0) {
            throw new CustomException(CustomError.FAQ_IDX_ERROR);
        }
        // 제목
        if (faqDto.getTitle() == null || faqDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.FAQ_TITLE_NULL);
        }
        // 공지 종류
        if (faqDto.getMenuIdx() == null || faqDto.getMenuIdx() < 0) {
            // 공지 종류를 입력해주세요
            throw new CustomException(CustomError.FAQ_MENU_NULL);
        }
        // 상태
        if (faqDto.getState() == null || faqDto.getState() < 0) {
            // 상태값을 입력해주세요
            throw new CustomException(CustomError.FAQ_STATE_NULL);
        }
        if (faqDto.getContents() == null || faqDto.getContents().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.FAQ_CONTENTS_NULL);
        }

        // 자주하는 질문 수정
        Integer iResult = updateFaq(faqDto);
        faqDto.setFaqIdx(faqDto.getIdx());
        if (iResult > 0) {
            // 자주하는 질문 제목 수정
            updateFaqTitle(faqDto);
            // 임시 폴더
            String tmpPath = SERVER + "/" + s3TmpUploadFolder;
            // s3에 저장될 path
            String s3Path = "faq/" + faqDto.getIdx();
            // 이미지 s3 upload, detail src 변경
            Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
            Matcher matcher = imgSrcPattern.matcher(faqDto.getContents());

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
                    faqDto.setContents(faqDto.getContents().replace(imgSrc, s3FullUrl));
                }
            }
            // 자주하는 질문 내용 수정
            updateFaqContents(faqDto);
        }

        // 관리자 action log
        adminActionLogService.regist(faqDto, Thread.currentThread().getStackTrace());
        return iResult;
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
     * 자주하는 질문 목록
     *
     * @param searchDto
     * @return 자주하는 질문 리스트
     */
    public List<FaqDto> getFaqList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = faqDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<FaqDto> list = faqDaoSub.getFaqList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 자주하는 질문 메뉴 목록
     *
     * @param faqDto
     * @return 자주하는 질문 메뉴 리스트
     */
    public List<FaqDto> getMenuList(FaqDto faqDto) {
        // 자주하는 질문 메뉴 리스트
        return faqDaoSub.getMenuList(faqDto);
    }

    /**
     * 자주하는 질문 상세
     *
     * @param idx
     * @return
     */
    public FaqDto getViewFaq(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.FAQ_IDX_ERROR);
        }
        // FAQ 상세
        FaqDto faqDto = faqDaoSub.getViewFaq(idx);
        // 이미지 리사이징
        String contents = thumborLibrary.replaceImgUrlToCFurl(faqDto.getContents(), 200, "common");
        faqDto.setContents(contents);

        return faqDto;
    }

    /*****************************************************
     *  SubFunction - insert
     ****************************************************/
    /**
     * 자주하는 질문 등록
     *
     * @param faqDto menuIdx title
     * @return insertedIdx
     */
    public Integer insertFaq(FaqDto faqDto) {
        faqDto.setRegDate(dateLibrary.getDatetime());
        faqDto.setAdminId(super.getAdminInfo("id"));

        faqDao.insertFaq(faqDto);
        return faqDto.getInsertedIdx();
    }

    /**
     * 자주하는 질문 제목 등록
     *
     * @param insertIdx faqIdx
     * @param faqDto    title
     */
    public void insertFaqTitle(Integer insertIdx, FaqDto faqDto) {
        faqDto.setFaqIdx(insertIdx);
        faqDto.setRegDate(dateLibrary.getDatetime());

        faqDao.insertFaqTitle(faqDto);
    }

    /**
     * 자주하는 질문 내용 등록
     *
     * @param insertIdx faqIdx
     * @param faqDto    contents
     */
    public void insertFaqContents(Integer insertIdx, FaqDto faqDto) {
        faqDto.setFaqIdx(insertIdx);
        faqDto.setRegDate(dateLibrary.getDatetime());

        faqDao.insertFaqContents(faqDto);
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/
    /**
     * 자주하는 질문 수정
     *
     * @param faqDto menuIdx title
     * @return affectedRow
     */
    public Integer updateFaq(FaqDto faqDto) {
        faqDto.setAdminId(super.getAdminInfo("id"));

        return faqDao.updateFaq(faqDto);
    }

    /**
     * 자주하는 질문 제목 수정
     *
     * @param faqDto faqIdx title
     */
    public void updateFaqTitle(FaqDto faqDto) {

        faqDao.updateFaqTitle(faqDto);
    }

    /**
     * 자주하는 질문 내용 수정
     *
     * @param faqDto faqIdx contents
     */
    public void updateFaqContents(FaqDto faqDto) {

        faqDao.updateFaqContents(faqDto);
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
    public void stateText(List<FaqDto> list) {
        for (FaqDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(FaqDto dto) {
        // 상태 (0 : 삭제 1: 정상)
        if (dto.getState() == 1) {  // 정상
            dto.setStateText(super.langMessage("lang.faq.state.normal"));
            dto.setStateBg("badge-success");
        } else {
            dto.setStateText(super.langMessage("lang.faq.state.delete"));
            dto.setStateBg("badge-danger");
        }
    }
}
