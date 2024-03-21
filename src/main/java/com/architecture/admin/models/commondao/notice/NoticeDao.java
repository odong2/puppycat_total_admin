package com.architecture.admin.models.commondao.notice;

import com.architecture.admin.models.dto.notice.NoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface NoticeDao {

    /*****************************************************
     * Insert
     ****************************************************/
    /**
     * 공지사항 등록
     *
     * @param noticeDto menuIdx isTop title adminId regdate
     */
    void insertNotice(NoticeDto noticeDto);

    /**
     * 공지사항 제목 등록
     *
     * @param noticeDto noticeIdx title regdate
     */
    void insertNoticeTitle(NoticeDto noticeDto);

    /**
     * 공지사항 내용 등록
     *
     * @param noticeDto noticeIdx contents regdate
     */
    Integer insertNoticeContents(NoticeDto noticeDto);

    /*****************************************************
     * Update
     ****************************************************/
    /**
     * 공지사항 수정
     *
     * @param noticeDto idx menuIdx isTop title adminId
     * @return affectedRow
     */
    Integer updateNotice(NoticeDto noticeDto);

    /**
     * 공지사항 제목 수정
     *
     * @param noticeDto noticeIdx title regdate
     */
    void updateNoticeTitle(NoticeDto noticeDto);

    /**
     * 공지사항 내용 수정
     *
     * @param noticeDto noticeIdx contents regdate
     */
    void updateNoticeContents(NoticeDto noticeDto);
}
