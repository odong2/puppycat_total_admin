package com.architecture.admin.services.login;

import com.architecture.admin.config.SessionConfig;
import com.architecture.admin.config.interceptor.JwtInterceptor;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.dao.admin.AdminDao;
import com.architecture.admin.models.daosub.admin.AdminDaoSub;
import com.architecture.admin.models.daosub.ip.IpDaoSub;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.services.BaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*****************************************************
 * 로그인 모델러
 ****************************************************/
@RequiredArgsConstructor
@Service
@Transactional
public class LoginService extends BaseService {

    private final AdminDao adminDao;
    private final AdminDaoSub adminDaoSub;
    private final IpDaoSub ipDaoSub;

    private final JwtInterceptor jwtInterceptor;

    /*****************************************************
     *  Modules
     ****************************************************/

    /**
     * 로그인
     *
     * @param adminDto     ( id / password )
     * @param httpRequest
     * @param httpResponse
     * @return true/false
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean login(AdminDto adminDto, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {

        String ip = super.getClientIP(httpRequest);

        // DB에 저장된 ip 주소가 맞는지 체크
        int ipCnt = ipDaoSub.checkIsAccessibleIP(ip);

        // 해당 ip를 찾지 못했을 경우
        if (ipCnt <= 0) {
            // 예외 처리
            String message = CustomError.ADMIN_IP_ACCESS_ERROR.getMessage();
            throw new Exception(message + " 현재 접근 중인 IP는 " + ip + " 입니다.");
        }

        // 아이디/패스워드 검증
        String id = adminDto.getId();
        String password = adminDto.getPassword();

        if (id == null || id.equals("")) {
            throw new CustomException(CustomError.LOGIN_ID_ERROR);
        }
        if (password == null || password.equals("")) {
            throw new CustomException(CustomError.LOGIN_PW_ERROR);
        }

        // 패스워드 암호화
        adminDto.setPassword(super.encrypt(password));

        // login
        AdminDto userInfo = adminDaoSub.getInfoForLogin(adminDto);

        if (userInfo != null && userInfo.getIdx() > 0) {

            if (userInfo.getState() == 2) { // 관리자 승인이 필요합니다
                throw new CustomException(CustomError.ADMIN_STATE_WAIT);
            }

            if (userInfo.getState() != 1) { // 계정 상태 확인 (정상 : 1)
                throw new CustomException(CustomError.ADMIN_STATE_ERROR);
            }

            // 마지막 로그인 날짜 업데이트
            userInfo.setLoginIp(ip);
            userInfo.setLastLoginDate(dateLibrary.getDatetime());
            adminDao.updateLastDate(userInfo);

            // 세션 생성
            session.setAttribute(SessionConfig.LOGIN_ID, userInfo.getId());
            // 관리자 정보 입력 Object -> json
            ObjectMapper objectMapper = new ObjectMapper();
            session.setAttribute(SessionConfig.ADMIN_INFO, objectMapper.writeValueAsString(userInfo));
            // 세션 만료 시간 설정
            session.setMaxInactiveInterval(SessionConfig.EXPIRED_TIME); // 7일

            // jwt access/refresh 토큰 생성 및 쿠키에 저장
            jwtInterceptor.setJwtToken(httpRequest, httpResponse);

            return true;
        } else {
            return false;
        }
    }

    // 해당 데이터 유효성 체크
    public boolean bAuthCheck(AdminDto adminDto) {
        int checkCount = adminDaoSub.getAuthCountById(adminDto);

        if (checkCount > 0) {
            return true;
        }

        return false;
    }
}
