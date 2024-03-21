package com.architecture.admin.config.interceptor;

import com.architecture.admin.config.SessionConfig;
import com.architecture.admin.libraries.DateLibrary;
import com.architecture.admin.libraries.TelegramLibrary;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.libraries.jwt.JwtDto;
import com.architecture.admin.libraries.jwt.JwtLibrary;
import com.architecture.admin.models.dao.jwt.JwtDao;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    // 해당 리스트에 포함된 경로 처리
    private static final String[] blacklist = {
            // api jwt 필터 적용 리스트
            "/v1/*",
    };
    private static final String[] whitelist = {
            "/v1/login/session"    // 로그인 세션 에러 페이지 [0] 번째 위치 고정
            , "/v1/login"          // 로그인
            , "/v1/join"           // 회원가입
            , "/robots.txt"
            , "/v1/check"          // 헬스 체크
            , "/v1/*/excel"           // 엑셀 다운
            , "/v1/*/sample-excel"    // 엑셀 양식 다운
            , "/v1/*/fail-excel"      // 엑셀 실패 다운
            , "/v1/*/tempImage"       // 에디터 사진 저장
//            , "/v1/inspect/*"           // 엑셀 다운
    };
    private final JwtLibrary jwtLibrary;
    private final JwtDto jwtDto = new JwtDto();
    @Value("${jwt.token.access.secret.key}")
    protected String accessType;
    @Value("${jwt.token.refresh.secret.key}")
    protected String refreshType;
    @Autowired
    protected DateLibrary dateLibrary;
    @Autowired
    protected HttpSession session;
    @Autowired
    private JwtDao jwtDao;

    private static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * JWT access/refresh 토큰 생성 및 쿠키 저장
     *
     * @param httpRequest
     * @param httpResponse
     */
    public void setJwtToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String sId = (String) session.getAttribute(SessionConfig.LOGIN_ID); // id 가져오기
        String ip = getClientIP(httpRequest); // ip 가져오기

        HashMap<String, Object> tokenMap = new HashMap<>(); // 토큰에 담을 정보
        tokenMap.put("ip", ip);
        tokenMap.put("id", sId);

        // 토큰생성
        JwtDto jwt = jwtLibrary.createToken(tokenMap);
        // 생성된 리프레쉬 토큰 db에 입력하기 (식별을위해 id와 ip 입력(ip는 중복가능,id는 유니크), 대조용 리프레쉬토큰자체 저장)
        tokenMap.put("refreshToken", jwt.getRefreshToken());
        // 날짜 세팅
        tokenMap.put("regDate", dateLibrary.getDatetime());

        // jwt 체크 후 있으면 인서트 없으면 업데이트
        Integer check = jwtDao.checkRefreshToken(tokenMap);

        if (check == 0) {
            jwtDao.insertRefreshToken(tokenMap);
        } else {
            jwtDao.updateRefreshToken(tokenMap);
        }

        Cookie accessToken = new Cookie("accessToken", jwt.getAccessToken()); // 쿠키 이름 지정하여 생성( key, value 개념)
        accessToken.setMaxAge(jwtDto.getCookieTime()); // 쿠키 유효 기간: 7일로 설정(168시간)
        accessToken.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        //accessToken.setHttpOnly(true);
        httpResponse.addCookie(accessToken);
    }

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
        String requestURI = httpRequest.getRequestURI();
        String ip = getClientIP(httpRequest);
        String sId = (String) session.getAttribute(SessionConfig.LOGIN_ID); // id 가져오기

        if (isJwtCheckPath(requestURI)) { // jwt토큰 검증 (블랙리스트)

            JwtDto jwtDto = new JwtDto();

            jwtDto.setAccessToken(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
            if (jwtDto.getAccessToken() == null || jwtDto.getAccessToken().equals("")) {
                throw new CustomException(CustomError.TOKEN_EMPTY_ERROR);
            }

            Integer iAccessTokenValue = jwtLibrary.validateAccessToken(jwtDto);

            if (iAccessTokenValue == 1) { // 유효한 경우
                // ip 확인
                String tokenIp = jwtLibrary.getIpFromToken(accessType, jwtDto.getAccessToken());

                if (!ip.equals(tokenIp)) {
                    iAccessTokenValue = 0;
                }
            }

            // access token이 유효하지 않을경우 -> 블락처리, 다시 발급받아야함
            if (iAccessTokenValue == 4) {
                throw new CustomException(CustomError.TOKEN_EXPIRE_TIME_ERROR);
            }

            // access token 유효하나 기간이 만료일경우 -> refresh token 확인
            if (iAccessTokenValue == 2) {
                // DB 에 있는 리프래시 값 조회
                HashMap<String, Object> reTokenMap = new HashMap<>(); // 토큰에 담을 정보
                reTokenMap.put("ip", ip);
                reTokenMap.put("id", sId);


                // session ID를 가지고 오지 못했을때 - curl 시도 시
                if (sId == null || sId.equals("")) {
                    // 다시 로그인 시도 요청
                    // 토큰이 만료되었습니다 재로그인후 다시 시도해주세요
                    throw new CustomException(CustomError.TOKEN_ID_ERROR);
                }

                String refreshToken = jwtDao.getRefreshToken(reTokenMap);
                jwtDto.setRefreshToken(refreshToken);


                // Refresh Token 검증 : 1일 경우 에만 새 access token가져옴
                Integer refreshChk = jwtLibrary.validateRefreshToken(jwtDto);

                if (refreshChk == 1) { // refresh가 유효하며 db와 일치하고 기간도 살아있을경우 access만 새로 쿠키에 등록
                    // ip 확인
                    String tokenIp = jwtLibrary.getIpFromToken(refreshType, jwtDto.getRefreshToken());
                    if (!ip.equals(tokenIp)) {
                        throw new CustomException(CustomError.TOKEN_EXPIRE_TIME_ERROR);
                    }

                    // db 확인절차 (db데이터와 일치되면 재발급)
                    HashMap tokenMap = (HashMap) jwtLibrary.getAllClaims(refreshType, jwtDto.getRefreshToken()).get("tokenMap");
                    String id = (String) tokenMap.get("id");
                    // db 조회하여 기존 refreshToken 및 id가 동일한지 확인
                    tokenMap.put("refreshToken", jwtDto.getRefreshToken());
                    tokenMap.put("id", id);
                    Integer verifyRefreshToken = jwtDao.verifyRefreshToken(tokenMap); // 일치:1, 불일치:0

                    if (verifyRefreshToken != 1) { // 불일치한 경우 블락처리
                        httpResponse.sendRedirect("/logout");
                    }

                    // RefreshToken 등록시간
                    int tokenReg = jwtLibrary.getRegdateFromToken(refreshType, jwtDto.getRefreshToken());
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    // RefreshToken 재 발급 기준 시간 (1일)
                    long refreshTime = (time.getTime() / 1000L) - (60 * 60 * 24);

                    // Token 재발급
                    if (tokenReg < refreshTime) {
                        // RefreshToken 발급시간이 하루 이상 지난경우 모두 재발급
                        setJwtToken(httpRequest, httpResponse);
                    } else {
                        // accessToken 재발급
                        Claims refreshClaim = jwtLibrary.getAllClaims(refreshType, jwtDto.getRefreshToken());
                        String newAccessToken = jwtLibrary.recreationAccessToken(refreshClaim.get("tokenMap"));
                        Cookie accessToken = new Cookie("accessToken", newAccessToken); // 쿠키 이름 지정하여 생성( key, value 개념)
                        accessToken.setMaxAge(jwtDto.getCookieTime()); // 쿠키 유효 기간: 7일
                        accessToken.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
                        //accessToken.setHttpOnly(true);
                        httpResponse.addCookie(accessToken);
                    }

                } else { // refresh 가 유효하지않거나 만료일경우 (새로 발급받아야함)
                    httpResponse.sendRedirect("/logout");
                }
            }
            // access, refresh 둘다 살아있을 경우 패스
        }

        return true;
    }

    /**
     * blackList 인증 체크
     * whiteList 목록 제외
     */
    private boolean isJwtCheckPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(blacklist, requestURI) && !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}