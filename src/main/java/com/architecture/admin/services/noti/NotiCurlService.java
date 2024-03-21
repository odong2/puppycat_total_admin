package com.architecture.admin.services.noti;

import com.architecture.admin.models.dto.noti.NotiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notiCurlService", url = "${member.domain}/v1/noti")
public interface NotiCurlService {

    /**
     * 공지 알림 등록
     *
     * @param token access token
     * @return
     */
    @PostMapping(value = "/notice")
    String registAdminNoti(@RequestHeader(value = "Authorization") String token,
                           @ModelAttribute NotiDto notiDto);

    /**
     * 알림 등록
     *
     * @param token
     * @param notiDto
     * @return
     */
    @PostMapping("/list")
    String registNotiList(@RequestHeader("Authorization") String token,
                          @RequestBody NotiDto notiDto);

}
