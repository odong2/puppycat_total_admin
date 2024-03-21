package com.architecture.admin.libraries.jwt;

import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;


/*****************************************************
 * JWT 라이브러리
 ****************************************************/
@Component
public class JwtLibrary {
    private static final String refreshSecretKey = Base64.getEncoder().encodeToString("puppycatTotalAdminRT".getBytes()); // static 해야함 들고있어야해서 여러개쓸때는 여러개만들어놓고, 인자값에서는 구분값가져와서 각자 다른 키 사용하도록
    private static final String accessSecretKey = Base64.getEncoder().encodeToString("puppycatTotalAdminAT".getBytes()); // static 해야함 들고있어야해서 여러개쓸때는 여러개만들어놓고, 인자값에서는 구분값가져와서 각자 다른 키 사용하도록

    private final JwtDto jwtDto = new JwtDto();

    /**
     * JWT 토큰 생성
     *
     * @param tokenMap
     * @return
     */
    public JwtDto createToken(Map<String, Object> tokenMap) {
        Claims claims = Jwts.claims().setSubject("info"); // JWT payload 에 저장되는 정보단위
        claims.put("tokenMap", tokenMap); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + jwtDto.getValidTime())) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)  // 사용할 암호화 알고리즘, signature 에 들어갈 secret값 세팅
                .compact(); // 위 설정대로 JWT 토큰 생성

        //Refresh Token
        String refreshToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + jwtDto.getValidTimeRefresh())) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)  // 사용할 암호화 알고리즘, signature 에 들어갈 secret값 세팅
                .compact(); // 위 설정대로 JWT 토큰 생성

        return JwtDto.builder().accessToken(accessToken).refreshToken(refreshToken).accessKey(accessSecretKey).refreshKey(refreshSecretKey).build();
    }

    /**
     * access token 검증
     *
     * @param accessTokenObj
     * @return
     */
    public Integer validateAccessToken(JwtDto accessTokenObj) {
        if (accessTokenObj.getAccessToken() == null || accessTokenObj.getAccessToken().equals("")) {
            return 4;
        }

        String accessToken = bearerRemove(accessTokenObj.getAccessToken());

        try {
            // 토큰 자체가 유효한 내용인지 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(accessToken);

            // 유효하며 access 토큰의 만료시간이 지나지 않았을 경우
            if (!claims.getBody().getExpiration().before(new Date())) {
                return 1;
            } else if (claims.getBody().getExpiration().before(new Date())) {// 유효하나 기간이 만료일경우
                return 2;
            }
        } catch (ExpiredJwtException e) {
            // 기간 만료
            return 2;
        } catch (SignatureException e) {
            // signature 인증 실패
            return 3;
        } catch (Exception e) {
            // access 토큰 에러(검증실패)
            return 4;
        }

        return 4; // 검증완료 - 도달할 경우 없음
    }

    /**
     * refresh token 검증
     *
     * @param refreshTokenObj
     * @return
     */
    public Integer validateRefreshToken(JwtDto refreshTokenObj) {
        // refresh 객체에서 refreshToken 추출
        String refreshToken = refreshTokenObj.getRefreshToken();
        try {
            // 토큰 자체가 유효한지 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);

            // refresh 토큰의 만료시간이 지나지 않았을 경우
            if (!claims.getBody().getExpiration().before(new Date())) {
                return 1;
            }
        } catch (ExpiredJwtException e) {
            // 기간 만료
            return 2;
        } catch (Exception e) {
            // signature 인증 실패
            return 0;
        }// refresh토큰 검증 오류(실패)

        return 0; //검증완료-실제로 도달하지않음
    }

    /**
     * access token 재발급 (refresh token이 유효한 경우 호출됨)
     *
     * @param tokenMap
     * @return
     */
    public String recreationAccessToken(Object tokenMap) {

        Claims claims = Jwts.claims().setSubject("info"); // JWT payload 에 저장되는 정보단위
        claims.put("tokenMap", tokenMap); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + jwtDto.getValidTime())) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)  // 사용할 암호화 알고리즘, signature 에 들어갈 secret값 세팅
                .compact(); // 위 설
    }

    /**
     * refreshToken 재발급 (refresh token이 유효시간 갱신이 필요한 경우 호출됨)
     *
     * @param tokenMap
     * @return
     */
    public String recreationRefreshToken(Object tokenMap) {

        Claims claims = Jwts.claims().setSubject("info"); // JWT payload 에 저장되는 정보단위
        claims.put("tokenMap", tokenMap); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        //Access Token
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + jwtDto.getValidTimeRefresh())) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    /**
     * 토큰의 Claim 디코딩
     *
     * @param token
     * @return
     */
    public Claims getAllClaims(String secretKeyType, String token) {
        String secretKeyUse = "";

        if ("access".equals(secretKeyType)) {
            secretKeyUse = accessSecretKey;
        } else if ("refresh".equals(secretKeyType)) {
            secretKeyUse = refreshSecretKey;
        }

        return Jwts.parser()
                .setSigningKey(secretKeyUse)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Claim 에서 ip 가져오기
     *
     * @param token
     * @return
     */
    public String getIpFromToken(String secretKeyType, String token) {
        token = bearerRemove(token);

        if (getAllClaims(secretKeyType, token).get("tokenMap") == null || getAllClaims(secretKeyType, token).get("tokenMap") == "") {
            // 토큰값이 비어있습니다
            throw new CustomException(CustomError.TOKEN_EMPTY_ERROR);
        }

        Map tokenMap = (Map) getAllClaims(secretKeyType, token).get("tokenMap");

        return (String) tokenMap.get("ip");
    }

    /**
     * Claim 에서 adminId 가져오기
     *
     * @param secretKeyType
     * @param token
     * @return
     */
    public String getAdminIdByToken(String secretKeyType, String token) {
        token = bearerRemove(token);

        if (getAllClaims(secretKeyType, token).get("tokenMap") == null || getAllClaims(secretKeyType, token).get("tokenMap") == "") {
            // 토큰 값 에러 다시 로그인 해주세요.
            throw new CustomException(CustomError.TOKEN_ERROR);
        }
        Map tokenMap = (Map) getAllClaims(secretKeyType, token).get("tokenMap");

        return String.valueOf(tokenMap.get("id"));
    }


    /**
     * Claim 에서 토큰만료시간 가져오기
     *
     * @param refreshSecretKeyType
     * @param token
     * @return
     */
    public int getExpirFromToken(String refreshSecretKeyType, String token) {
        return (int) getAllClaims(refreshSecretKeyType, token).get("exp");
    }

    /**
     * Claim 에서 토큰등록시간 가져오기
     *
     * @param refreshSecretKeyType
     * @param token
     * @return
     */
    public int getRegdateFromToken(String refreshSecretKeyType, String token) {
        return (int) getAllClaims(refreshSecretKeyType, token).get("iat");
    }

    /**
     * 토큰 앞 부분('Bearer ') 제거
     *
     * @param token
     * @return
     */
    private String bearerRemove(String token) {
        // 0-7 번째 글자 자르기
        String sSubToken = token.substring(0, 7);
        // 자른 문자가 Bearer  이면 제거
        if (sSubToken.equals("Bearer ")) {
            token = token.substring("Bearer ".length());
        }

        return token;
    }

}
