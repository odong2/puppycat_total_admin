package com.architecture.admin.config;

public interface SessionConfig {
    String LOGIN_ID = "id";
    String LOGIN_NICK = "nick";
    String ADMIN_LEVEL = "adminLevel";
    String ADMIN_INFO = "adminInfo";
    Integer EXPIRED_TIME = 60 * 60 * 24 * 7; // 일주일
}