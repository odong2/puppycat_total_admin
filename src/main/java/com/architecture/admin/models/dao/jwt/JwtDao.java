package com.architecture.admin.models.dao.jwt;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Mapper
@Repository
public interface JwtDao {
    /**
     * jwt refresh토큰 검증
     *
     * @param tokenMap
     * @return
     */
    Integer verifyRefreshToken(HashMap tokenMap);

    /**
     * jwt refresh토큰 유무 체크
     *
     * @param tokenMap
     * @return
     */
    Integer checkRefreshToken(HashMap tokenMap);


    /**
     * jwt refresh토큰 저장
     *
     * @param tokenMap
     * @return
     */
    Integer insertRefreshToken(HashMap tokenMap);

    /**
     * jwt refresh토큰 수정
     *
     * @param tokenMap
     * @return
     */
    Integer updateRefreshToken(HashMap tokenMap);

    /**
     * id ip 로 리프레시 토큰 조회
     *
     * @param tokenMap
     * @return
     */
    String getRefreshToken(HashMap tokenMap);

    /**
     * jwt refresh 토큰 삭제
     *
     * @param tokenMap
     * @return
     */
    Integer deleteRefreshToken(HashMap tokenMap);
}


