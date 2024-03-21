package com.architecture.admin.libraries.filter;

import com.architecture.admin.config.SessionConfig;
import com.architecture.admin.libraries.TelegramLibrary;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*****************************************************
 * 로그인 인증 체크 필터
 ****************************************************/
@Component
@Slf4j
public class LoginCheckFilterLibrary implements Filter {
    private static final String[] whitelist = {
            // html
            "/css/*",
            "/js/*",
            "/images/*",
            "/components/*",
            // 회원 가입
            "/join",
            // 회원 로그인
            "/login",
            // 로그아웃
            "/logout",
            "/v1/*",
            "/robots.txt",
            "/check"                // 헬스 체크
    };

    @Autowired
    private TelegramLibrary telegramLibrary;

    // 생성자 추가
    public LoginCheckFilterLibrary() {
        this.telegramLibrary = new TelegramLibrary();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String scheme = request.getScheme(); // http / https
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String serverName = request.getServerName();// 도메인만
        int serverPort = request.getServerPort();// 포트

        String currentDomain = scheme + "://" + serverName + ":" + serverPort; // 전체 도메인

        String redirectURL = currentDomain + "/login?redirectURL=" + requestURI;


        if (isLoginCheckPath(requestURI)) {
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute(SessionConfig.LOGIN_ID) == null) {
                // 로그인으로 리다이렉트
                httpResponse.sendRedirect(redirectURL);
                return;
            }

            // 회원 로그인 상태 확인
            ObjectMapper mapper = new ObjectMapper();
            String sAdminInfo = (String) session.getAttribute(SessionConfig.ADMIN_INFO);
            if (sAdminInfo == null) {
                // 로그인으로 리다이렉트
                httpResponse.sendRedirect(redirectURL);
                return;
            }

            // 관리자 정보 확인  json -> Object
            AdminDto admin = mapper.readValue(sAdminInfo, AdminDto.class);
            if (session.getAttribute(SessionConfig.LOGIN_ID) == null || admin.getState() != 1) {
                // 로그인으로 리다이렉트
                httpResponse.sendRedirect(redirectURL);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * whiteList 인증 체크 패스
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}