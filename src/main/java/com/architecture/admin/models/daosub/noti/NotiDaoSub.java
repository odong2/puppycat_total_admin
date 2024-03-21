package com.architecture.admin.models.daosub.noti;

import com.architecture.admin.models.dto.noti.NotiDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotiDaoSub {
    /**
     * sns_member_notification 데이터 가져오기
     * 해당 알림을 보냈는지 확인용
     *
     * @param notiDto memberidx sender_idx subtype
     * @return noti.idx
     */
    Long getNotiCheck(NotiDto notiDto);

    /**
     * 마지막으로 알림확인한 날짜 가져오기
     *
     * @param notiDto memberIdx
     * @return 날짜
     */
    String getNotiShowDate(NotiDto notiDto);

    /**
     * 회원별 신규 알림 카운트
     *
     * @param notiDto memberIdx showDate
     * @return count
     */
    Integer getCountNewNoti(NotiDto notiDto);

    /**
     * 신규 공지 알림 카운트
     *
     * @param notiDto  showDate
     * @return count
     */
    Integer getCountNewNoticeNoti(NotiDto notiDto);

    /**
     * 알림용 회원의 팔로워 리스트 가져오기
     *
     * @param notiDto senderIdx
     * @return 팔로워리스트
     */
    List<Long> getFollowerList(NotiDto notiDto);
}
