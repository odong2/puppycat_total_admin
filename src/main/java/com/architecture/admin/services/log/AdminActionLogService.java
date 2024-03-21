package com.architecture.admin.services.log;

import com.architecture.admin.libraries.ServerLibrary;
import com.architecture.admin.models.dao.log.AdminActionLogDao;
import com.architecture.admin.models.daosub.log.AdminActionLogDaoSub;
import com.architecture.admin.models.dto.log.AdminActionLogDto;
import com.architecture.admin.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/*****************************************************
 *  admin action log 모델러
 ****************************************************/
@Service
public class AdminActionLogService extends BaseService {

    // mainDB
    @Autowired
    AdminActionLogDao adminActionLogDao;

    // subDB
    @Autowired
    AdminActionLogDaoSub adminActionLogDaoSub;

    // 생성자 추가
    AdminActionLogDto adminActionLogDto = new AdminActionLogDto();

    /*****************************************************
     *  Modules
     ****************************************************/
    /**
     * admin action log 저장
     *
     * @param params
     *              입력/수정 전달값
     * @param arrStackTrace
     *              실행 메소드 정보
     */
    public void regist(Object params,StackTraceElement[] arrStackTrace) {

        HttpServletRequest request = ServerLibrary.getCurrReq();
        String referrer = request.getHeader("Referer");
        String clientIp = super.getClientIP(request);
        String sClass = arrStackTrace[1].getClassName();
        String method = arrStackTrace[1].getMethodName();

        adminActionLogDto.setId(super.getAdminInfo("id"));// SESSION 정보 이용하여 처리한 관리자 id 추출
        adminActionLogDto.setRegDate(dateLibrary.getDatetime());// 등록시간
        adminActionLogDto.setReferrer(referrer);// referrer
        adminActionLogDto.setSClass(sClass);// 호출 class
        adminActionLogDto.setMethod(method);// 호출 method
        adminActionLogDto.setParams(params.toString());// 전달 params
        adminActionLogDto.setIp(clientIp);// ip
        adminActionLogDao.regist(adminActionLogDto);
    }
}
