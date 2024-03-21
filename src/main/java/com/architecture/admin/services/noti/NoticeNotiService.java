package com.architecture.admin.services.noti;

import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dto.noti.NotiDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*****************************************************
 * 어드민 알림 모델러
 ****************************************************/
@Service
@RequiredArgsConstructor
public class NoticeNotiService extends BaseService {

    private final NotiCurlService notiCurlService;

    /*****************************************************
     *  Modules
     ****************************************************/
    public String sendNoti(String token, NotiDto notiDto) {
        if (notiDto.getSubType() == null || notiDto.getSubType().equals("")) {
            // 서브타입을 입력해주세요.
            throw new CustomException(CustomError.NOTI_SUBTYPE_ERROR);
        }
        if (notiDto.getContentsIdx() <= 0) {
            // 알림 보낼 컨텐츠 IDX가 비어있습니다.
            throw new CustomException(CustomError.NOTI_CONTENTSIDX_ERROR);
        }
        if (notiDto.getBody() == null || notiDto.getBody().equals("")) {
            // 알림 내용이 비어있습니다
            throw new CustomException(CustomError.NOTI_BODY_ERROR);
        }
        if (notiDto.getTitle() == null || notiDto.getTitle().equals("")) {
            // 알림 제목이 비어있습니다.
            throw new CustomException(CustomError.NOTI_TITLE_ERROR);
        }

       return notiCurlService.registAdminNoti(token, notiDto);
    }
}
