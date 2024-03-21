package com.architecture.admin.services.notice;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.S3Library;
import com.architecture.admin.libraries.ThumborLibrary;
import com.architecture.admin.libraries.exception.CurlException;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.commondao.notice.NoticeDao;
import com.architecture.admin.models.daosub.notice.NoticeDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.noti.NotiDto;
import com.architecture.admin.models.dto.notice.NoticeDto;
import com.architecture.admin.services.BaseService;
import com.architecture.admin.services.log.AdminActionLogService;
import com.architecture.admin.services.noti.NoticeNotiService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*****************************************************
 * 공지사항 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
@Transactional(value = "dbcommonmaintransactionManager")
public class NoticeService extends BaseService {
    private final NoticeDao noticeDao;
    private final NoticeDaoSub noticeDaoSub;
    private final S3Library s3Library;
    private final AdminActionLogService adminActionLogService;   // 관리자 action log 처리용
    private final ThumborLibrary thumborLibrary;
    private final NoticeNotiService adminNotiService;

    // 설정된 S3 tmpUpload 폴더
    @Value("${cloud.aws.s3.tmpFolder}")
    private String s3TmpUploadFolder;
    @Value("${env.server}")
    private String SERVER;
    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 공지사항 등록
     *
     * @param noticeDto menuIdx isTop title adminId regdate
     * @return insertedIdx
     */
    public Integer registNotice(String token, NoticeDto noticeDto) {

        // validate
        // 제목
        if (noticeDto.getTitle() == null || noticeDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.NOTICE_TITLE_NULL);
        }
        // 상단공지
        if (noticeDto.getIsTop() == null || noticeDto.getIsTop() < 0) {
            noticeDto.setIsTop(0);
        }
        // 공지 종류
        if (noticeDto.getMenuIdx() == null || noticeDto.getMenuIdx() < 0) {
            // 공지 종류를 입력해주세요
            throw new CustomException(CustomError.NOTICE_MENU_NULL);
        }
        if (noticeDto.getContents() == null || noticeDto.getContents().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.NOTICE_CONTENTS_NULL);
        }

        if (noticeDto.getIsNoti() == 1 && noticeDto.getBody().equals("")) {
            // 알림 내용을 입력해주세요
            throw new CustomException(CustomError.NOTICE_NOTI_BODY_NULL);
        }

        // 공지사항 등록
        Integer iInsertIdx = insertNotice(noticeDto);
        // 공지사항 제목 등록
        insertNoticeTitle(iInsertIdx, noticeDto);
        // 임시 폴더
        String tmpPath = SERVER + "/" + s3TmpUploadFolder;
        // s3에 저장될 path
        String s3Path = "notice/" + iInsertIdx;
        // 이미지 s3 upload, detail src 변경
        Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
        Matcher matcher = imgSrcPattern.matcher(noticeDto.getContents());

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
            noticeDto.setContents(noticeDto.getContents().replace(imgSrc, s3FullUrl));
        }

        // 공지사항 내용 등록
        int result = insertNoticeContents(iInsertIdx, noticeDto);

        // 알림 사용 이면 알림 저장
        if (result > 0 && noticeDto.getIsNoti() == 1) {
            String title;
            String subType;
            if (noticeDto.getMenuIdx() == 1) {
                title = "공지";
                subType = "notice";
            } else {
                title = "이벤트";
                subType = "event";
            }

            // 알림 등록
            NotiDto notiDto = NotiDto.builder()
                    .contentsIdx(noticeDto.getInsertedIdx())   // 컨텐츠 IDX
                    .subType(subType)
                    .body(noticeDto.getBody())
                    .title(title)
                    .build();

            String sendNoticeNoti = adminNotiService.sendNoti(token, notiDto);
            JSONObject sendNoticeNotiObject = new JSONObject(sendNoticeNoti);

            if (!((boolean) sendNoticeNotiObject.get("result"))) {
                throw new CurlException(sendNoticeNotiObject);
            }
        }

        // 관리자 action log
        adminActionLogService.regist(noticeDto, Thread.currentThread().getStackTrace());

        return iInsertIdx;
    }

    /**
     * 공지사항 수정
     *
     * @param noticeDto idx title isTop menuIdx state
     * @return
     */
    public Integer modifyNotice(NoticeDto noticeDto) {
        // validate
        if (noticeDto.getIdx() == null || noticeDto.getIdx() < 0) {
            throw new CustomException(CustomError.NOTICE_IDX_ERROR);
        }
        // 제목
        if (noticeDto.getTitle() == null || noticeDto.getTitle().equals("")) {
            // 제목을 입력해주세요
            throw new CustomException(CustomError.NOTICE_TITLE_NULL);
        }
        // 상단공지
        if (noticeDto.getIsTop() == null || noticeDto.getIsTop() < 0) {
            noticeDto.setIsTop(0);
        }
        // 공지 종류
        if (noticeDto.getMenuIdx() == null || noticeDto.getMenuIdx() < 0) {
            // 공지 종류를 입력해주세요
            throw new CustomException(CustomError.NOTICE_MENU_NULL);
        }
        // 상태
        if (noticeDto.getState() == null || noticeDto.getState() < 0) {
            // 상태값을 입력해주세요
            throw new CustomException(CustomError.NOTICE_STATE_NULL);
        }
        if (noticeDto.getContents() == null || noticeDto.getContents().equals("")) {
            // 내용을 입력해주세요
            throw new CustomException(CustomError.NOTICE_CONTENTS_NULL);
        }

        // 공지사항 수정
        Integer iResult = updateNotice(noticeDto);
        noticeDto.setNoticeIdx(noticeDto.getIdx());
        if (iResult > 0) {
            // 공지사항 제목 수정
            updateNoticeTitle(noticeDto);
            // 임시 폴더
            String tmpPath = SERVER + "/" + s3TmpUploadFolder;
            // s3에 저장될 path
            String s3Path = "notice/" + noticeDto.getIdx();
            // 이미지 s3 upload, detail src 변경
            Pattern imgSrcPattern = Pattern.compile("(<img[^>]*src\s*=\s*[\"']?([^>\"\']+)[\"']?[^>]*>)");
            Matcher matcher = imgSrcPattern.matcher(noticeDto.getContents());

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
                    noticeDto.setContents(noticeDto.getContents().replace(imgSrc, s3FullUrl));
                }
            }
            // 공지사항 내용 수정
            updateNoticeContents(noticeDto);
        }

        // 관리자 action log
        adminActionLogService.regist(noticeDto, Thread.currentThread().getStackTrace());

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
     * 공지사항 목록
     *
     * @param searchDto
     * @return 공지사항 리스트
     */
    public List<NoticeDto> getList(SearchDto searchDto) {
        // 목록 전체 count
        int totalCount = noticeDaoSub.getTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        // 회원리스트
        List<NoticeDto> list = noticeDaoSub.getList(searchDto);

        if (!list.isEmpty()) {
            // 문자변환
            stateText(list);
        }

        return list;
    }

    /**
     * 공지사항 메뉴 목록
     *
     * @param noticeDto
     * @return 공지사항 메뉴 리스트
     */
    public List<NoticeDto> getMenuList(NoticeDto noticeDto) {
        // 공지사항 메뉴 리스트
        return noticeDaoSub.getMenuList(noticeDto);
    }

    /**
     * 공지사항 상세
     *
     * @param idx
     * @return
     */
    public NoticeDto getViewNotice(Integer idx) {
        if (idx == null || idx < 1) {
            throw new CustomException(CustomError.NOTICE_IDX_ERROR);
        }
        // 회원 상세
        NoticeDto noticeDto = noticeDaoSub.getViewNotice(idx);
        // 이미지 리사이징
        String contents = thumborLibrary.replaceImgUrlToCFurl(noticeDto.getContents(), 200, "common");
        noticeDto.setContents(contents);

        return noticeDto;
    }


    /*****************************************************
     *  SubFunction - insert
     ****************************************************/
    /**
     * 공지사항 등록
     *
     * @param noticeDto menuIdx isTop title
     * @return insertedIdx
     */
    public Integer insertNotice(NoticeDto noticeDto) {
        noticeDto.setRegDate(dateLibrary.getDatetime());
        noticeDto.setAdminId(super.getAdminInfo("id"));

        noticeDao.insertNotice(noticeDto);
        return noticeDto.getInsertedIdx();
    }

    /**
     * 공지사항 제목 등록
     *
     * @param insertIdx noticeIdx
     * @param noticeDto title
     */
    public void insertNoticeTitle(Integer insertIdx, NoticeDto noticeDto) {
        noticeDto.setNoticeIdx(insertIdx);
        noticeDto.setRegDate(dateLibrary.getDatetime());

        noticeDao.insertNoticeTitle(noticeDto);
    }

    /**
     * 공지사항 내용 등록
     *
     * @param insertIdx noticeIdx
     * @param noticeDto contents
     */
    public Integer insertNoticeContents(Integer insertIdx, NoticeDto noticeDto) {
        noticeDto.setNoticeIdx(insertIdx);
        noticeDto.setRegDate(dateLibrary.getDatetime());

        return noticeDao.insertNoticeContents(noticeDto);
    }

    /*****************************************************
     *  SubFunction - Update
     ****************************************************/
    /**
     * 공지사항 수정
     *
     * @param noticeDto menuIdx isTop title
     * @return affectedRow
     */
    public Integer updateNotice(NoticeDto noticeDto) {
        noticeDto.setAdminId(super.getAdminInfo("id"));

        return noticeDao.updateNotice(noticeDto);
    }

    /**
     * 공지사항 제목 수정
     *
     * @param noticeDto noticeIdx title
     */
    public void updateNoticeTitle(NoticeDto noticeDto) {

        noticeDao.updateNoticeTitle(noticeDto);
    }

    /**
     * 공지사항 내용 수정
     *
     * @param noticeDto noticeIdx contents
     */
    public void updateNoticeContents(NoticeDto noticeDto) {

        noticeDao.updateNoticeContents(noticeDto);
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
    public void stateText(List<NoticeDto> list) {
        for (NoticeDto l : list) {
            stateText(l);
        }
    }

    /**
     * 문자변환 dto
     *
     * @param dto
     */
    public void stateText(NoticeDto dto) {
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
