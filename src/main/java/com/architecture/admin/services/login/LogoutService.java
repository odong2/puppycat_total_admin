package com.architecture.admin.services.login;

import com.architecture.admin.config.SessionConfig;
import com.architecture.admin.models.dao.jwt.JwtDao;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LogoutService extends BaseService {

    private final JwtDao jwtDao;

    /**
     * 로그아웃
     */
    public boolean logout(HttpServletRequest httpRequest,
                          HttpServletResponse httpResponse) {
        String memberId = (String) session.getAttribute(SessionConfig.LOGIN_ID); // id 가져오기
        String ip = getClientIP(httpRequest);

        HashMap<String, Object> tokenMap = new HashMap<>(); // 토큰 업데이트용
        tokenMap.put("memberId", memberId);
        tokenMap.put("ip", ip);

        // 세션 아이디 가져오기
        String sId = session.getId();
        if (!Objects.equals(sId, "")) {
            // 레디스 세션 정보 삭제
            String sSessionKey = "spring:session:sessions:" + sId;
            String sExpiresKey = "spring:session:sessions:expires:" + sId;
            super.removeRedis(sSessionKey);
            super.removeRedis(sExpiresKey);

            // 세션 비활성화
            session.invalidate();
        }

        // 쿠키 삭제
        Cookie accessToken = new Cookie("accessToken", null);
        accessToken.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        accessToken.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        httpResponse.addCookie(accessToken);

        // jwt refresh 토큰 빈 값 업데이트
        Integer iResult = jwtDao.deleteRefreshToken(tokenMap);

        return iResult > 0;
    }
}
