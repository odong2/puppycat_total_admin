package com.architecture.admin.services;

import com.architecture.admin.config.SessionConfig;
import com.architecture.admin.libraries.*;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.block.BlockMemberDaoSub;
import com.architecture.admin.models.daosub.member.MemberDaoSub;
import com.architecture.admin.models.dto.admin.AdminDto;
import com.architecture.admin.models.dto.block.BlockMemberDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.architecture.admin.config.SessionConfig.ADMIN_INFO;

/*****************************************************
 * 코어 서비스
 ****************************************************/
@Service
public class BaseService {
    // 시간 라이브러리 참조
    @Autowired
    protected DateLibrary dateLibrary;

    // 암호화 라이브러리
    @Autowired
    protected SecurityLibrary securityLibrary;

    // 세션
    @Autowired
    protected HttpSession session;

    // 텔레그램
    @Autowired
    protected TelegramLibrary telegramLibrary;

    // Redis 라이브러리
    @Autowired
    protected RedisLibrary redisLibrary;

    // Curl 라이브러리
    @Autowired
    protected CurlLibrary curlLibrary;

    /**
     * 메시지 가져오는 라이브러리
     */
    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected MemberDaoSub memberDaoSub;

    @Autowired
    private BlockMemberDaoSub blockMemberDaoSub;

    public BaseService() {
    }

    /*****************************************************
     * 회원정보 레디스 불러오기
     * 리턴되는 회원정보가 String 형식이며, 사용시 불편 => ADMIN_INFO session 사용 할 것
     ****************************************************/
    public String getMemberInfo() {
        // 세션 생성
        String sId = (String) session.getAttribute(SessionConfig.LOGIN_ID);

        // 세션 정보 레디스에 적용
        String sKey = "session_" + sId;
        return getRedis(sKey);
    }

    /*****************************************************
     * 회원정보 SESSION 불러오기
     * session 의 SessionConfig.ADMIN_INFO 이용
     * {"idx":1,"adminIdx":22,"id":"test@test.com","level":1,"state":1,"lastLoginDate":"2022-10-20 14:14:39","regdate":"2022-10-18 17:35:42","insertedId":null,"lastDateRow":null}
     ****************************************************/
    public AdminDto getAdminSessionInfo() throws JsonProcessingException {
        // 세션 생성
        ObjectMapper mapper = new ObjectMapper();
        String sAdminInfo = String.valueOf(session.getAttribute(SessionConfig.ADMIN_INFO));

        return mapper.readValue(sAdminInfo, AdminDto.class);// AdminDto
    }

    /*****************************************************
     * 레디스
     ****************************************************/
    // 레디스 값 생성
    public void setRedis(String key, String value, Integer expiredSeconds) {
        redisLibrary.setData(key, value, expiredSeconds);
    }

    // 레디스 값 불러오기
    public String getRedis(String key) {
        return redisLibrary.getData(key);
    }

    // 레디스 값 삭제하기
    public void removeRedis(String key) {
        redisLibrary.deleteData(key);
    }

    /*****************************************************
     * Curl
     ****************************************************/
    // get
    public String getCurl(String url) {
        return curlLibrary.get(url);
    }

    // post
    public String postCurl(String url, Map dataset) {
        return curlLibrary.post(url, dataset);
    }

    /*****************************************************
     * 암호화 처리
     ****************************************************/
    // 양방향 암호화 암호화
    public String encrypt(String str) throws Exception {
        return securityLibrary.aesEncrypt(str);
    }

    // 양방향 암호화 복호화
    public String decrypt(String str) throws Exception {
        return securityLibrary.aesDecrypt(str);
    }

    // 단방향 암호화
    public String md5encrypt(String str) {
        return securityLibrary.md5Encrypt(str);
    }

    /*****************************************************
     * 디버깅
     ****************************************************/
    public void d() {
        int iSeq = 2;
        System.out.println("======================================================================");
        System.out.println("클래스명 : " + Thread.currentThread().getStackTrace()[iSeq].getClassName());
        System.out.println("메소드명 : " + Thread.currentThread().getStackTrace()[iSeq].getMethodName());
        System.out.println("줄번호 : " + Thread.currentThread().getStackTrace()[iSeq].getLineNumber());
        System.out.println("파일명 : " + Thread.currentThread().getStackTrace()[iSeq].getFileName());
    }

    /*****************************************************
     * Language 값 가져오기
     ****************************************************/
    public String langMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String langMessage(String code, @Nullable Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /*****************************************************
     * 세션 값 가져오기
     ****************************************************/
    public String getSession(String id) {
        return (String) session.getAttribute(id);
    }

    /*****************************************************
     * 관리자 정보 불러오기
     ****************************************************/
    public String getAdminInfo(String key) {

        JSONObject json = new JSONObject(getSession(SessionConfig.ADMIN_INFO));

        return json.getString(key);
    }

    public Integer getAdminIdx(String key) {

        JSONObject json = new JSONObject(getSession(SessionConfig.ADMIN_INFO));

        return json.getInt(key);
    }

    /*****************************************************
     * get locale Language 현재 언어 값
     ****************************************************/
    public String getLocaleLang() {
        String localLang = LocaleContextHolder.getLocale().toString().toLowerCase();

        switch (localLang) {
            case "ko_kr", "ko", "kr":
                return "ko";
            case "en":
                return "en";
            default:
                return "en";
        }
    }

    /*****************************************************
     * ip 값 가져오기
     * private => public으로 변환
     ****************************************************/
    public String getClientIP(HttpServletRequest request) {
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

    public void pushAlarm(String sendMessage) {
        telegramLibrary.sendMessage(sendMessage);
    }

    public void pushAlarm(String sendMessage, String sChatId) {
        telegramLibrary.sendMessage(sendMessage, sChatId);
    }

    /*****************************************************
     * 적용 설정 언어
     * 현재 적용된 lang 리턴
     ****************************************************/
    public String getLocalLang() {
        return String.valueOf(LocaleContextHolder.getLocale());
    }

    /*****************************************************
     * 컨텐츠 멘션 복호화
     ****************************************************/
    public String mentionDecrypt(String text) {

        Pattern mentionPattern = Pattern.compile("\\[@\\[[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|_|.]*\\]]");
        Matcher mentionMatcher = mentionPattern.matcher(text);
        String extractMention = null;
        String mention = null;
        String nick = null;

        while (mentionMatcher.find()) {

            extractMention = mentionMatcher.group(); // 패턴에 일치하는 문자열 반환 ex) [@[ko07a1b7553]]
            mention = extractMention.substring(3, extractMention.length() - 2); // [@[, ]] 제거

            // UUID에 해당하는 회원 닉네임 조회
            nick = memberDaoSub.getMemberNickByUuid(mention);

            // 일치하는 닉네임 있다면 @닉네임 형식으로 변환
            if (nick != null) {
                // ex) [@[ko07a1b7553]] > @냐옹이
                text = text.replace(extractMention, "@" + nick);
            } else { // 탈퇴 테이블에서 조회
                nick = memberDaoSub.getOutMemberNickByUuid(mention);
                if (nick != null) {
                    text = text.replace(extractMention, "@" + nick);
                } else {
                    // 완전 없는 회원이면 @로 노출
                    text = text.replace(extractMention, "@" + mention);
                }
            }
        }
        return text;
    }

    /*****************************************************
     * 해시태그 복호화
     ****************************************************/
    public String tagDecrypt(String text) {

        Pattern tagPattern = Pattern.compile("\\[#\\[[_\\p{So}\\p{L}\\p{M}\\p{N}\\uD83C-\\uDBFF\\uDC00-\\uDFFF]*]*\\]]");
        Matcher tagMatcher = tagPattern.matcher(text);

        while (tagMatcher.find()) {
            // 패턴에 일치하는 문자열 반환 ex) [#[태그]]
            String extractTag = tagMatcher.group();
            // 문자만 추출
            String hashTag = extractTag.substring(3, extractTag.length() - 2);
            // 패턴 -> 해시태그로 변환
            text = text.replace(extractTag, "#" + hashTag);
        }
        return text;
    }

    /*****************************************************
     * 한명이라도 차단 한/된 상태인지 체크
     ****************************************************/
    public boolean bChkBlock(String memberUuid, String blockUuid) {
        // 차단 관련 데이터 set
        BlockMemberDto blockMemberDto = new BlockMemberDto();
        blockMemberDto.setMemberUuid(memberUuid);
        blockMemberDto.setBlockUuid(blockUuid);
        int check1 = blockMemberDaoSub.getBlockByIdx(blockMemberDto);

        blockMemberDto.setMemberUuid(blockUuid);
        blockMemberDto.setBlockUuid(memberUuid);
        int check2 = blockMemberDaoSub.getBlockByIdx(blockMemberDto);

        boolean result = false;
        if (check1 > 0 || check2 > 0) {
            result = true;
        }
        return result;
    }

    /**
     * 세션에 저장된 관리자 접근 권한 체크
     * 7레벨 이하일 경우 등록, 삭제, 수정 불가
     */
    public void checkAdminAccessLevel() {

        // 로그인한 관리자 정보
        JSONObject adminInfo = new JSONObject(session.getAttribute(ADMIN_INFO).toString());

        // 관리자의 접근 레벨 정보
        Integer adminLevel = (Integer) adminInfo.get("level");

        // 현재 로그인한 관리자의 레벨이 7레벨보다 낮은 경우
        if (adminLevel < 7) {

            // 오류 발생시 텔레그램 알람, 메인페이지 이동 처리
            throw new CustomException(CustomError.ACCESS_FAIL);
        }
    }
}
