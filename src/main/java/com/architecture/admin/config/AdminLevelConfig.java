package com.architecture.admin.config;

import java.util.HashMap;
import java.util.Map;

/*****************************************************
 * 관리자 레벨 설정
 ****************************************************/
public class AdminLevelConfig{

    public static Map<Integer, String> getAdminLevel() {

        HashMap<Integer, String> hmAdminLevel = new HashMap<>();

        hmAdminLevel.put(0, "Lv0"); // 승인대기
        hmAdminLevel.put(1, "Lv1"); // CS
        hmAdminLevel.put(2, "Lv2"); // 운영
        hmAdminLevel.put(3, "Lv3"); // 운영 관리자
        hmAdminLevel.put(4, "Lv4"); // 정산 관리자
        // level 7 이상 메뉴 수정 권한 줌
        hmAdminLevel.put(7, "Lv7");
        hmAdminLevel.put(8, "Lv8");
        hmAdminLevel.put(9, "Lv9"); // 개발

        return hmAdminLevel;
    }
}
