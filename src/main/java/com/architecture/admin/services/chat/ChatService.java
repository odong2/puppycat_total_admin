package com.architecture.admin.services.chat;

import com.architecture.admin.libraries.PaginationLibray;
import com.architecture.admin.libraries.exception.CustomError;
import com.architecture.admin.libraries.exception.CustomException;
import com.architecture.admin.models.daosub.chat.ChatDaoSub;
import com.architecture.admin.models.dto.SearchDto;
import com.architecture.admin.models.dto.chat.ChatMessageReportDto;
import com.architecture.admin.services.BaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService extends BaseService {

    private final ChatDaoSub chatDaoSub;
    private final StringRedisTemplate redisTemplate;

    /*****************************************************
     *  SubFunction - select
     ****************************************************/
    // 채팅 신고 내역 리스트
    public List<ChatMessageReportDto> getChatReportList(SearchDto searchDto) {

        List<ChatMessageReportDto> getList = null;

        // 날짜 유효성 체크 및 입력 된 날짜 있을 경우 time 23:59:59 setting
        dateLibrary.dateValidator(searchDto);

        // 목록 전체 count
        int totalCount = chatDaoSub.getChatReportTotalCount(searchDto);

        // paging
        PaginationLibray pagination = new PaginationLibray(totalCount, searchDto);
        searchDto.setPagination(pagination);

        System.out.printf("요기?");

        if (totalCount > 0) {
            getList = chatDaoSub.getChatReportList(searchDto);
        }

        return getList;
    }

    // 채팅 신고 내역 상세
    public List<ChatMessageReportDto> getChatReportView(Integer idx) {

        return chatDaoSub.getChatReportView(idx);
    }

    /**
     * LOG DATA RETURN
     */
    public Object getRoomLog(ChatMessageReportDto chatMessageReportDto) throws JsonProcessingException {
        SearchDto searchDto = new SearchDto();
        searchDto.setRecordSize(100);
        Long targetRank = Long.valueOf(0);

        // roomUuid 존재 하는지 체크
        if (chatMessageReportDto.getRoomUuid() == null || chatMessageReportDto.getRoomUuid().equals("")) {
            throw new CustomException(CustomError.LANG_MEMBER_ERROR); // Chat Room Uuid 유효성에 오류가 발생 하였습니다.
        }

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String key = "log::" + chatMessageReportDto.getRoomUuid() + "_" + chatMessageReportDto.getMemberUuid();

        // 총 채팅 로그 갯수
        Long lTotalCount = zSetOperations.size(key);
        Set<String> rankObject = zSetOperations.rangeByScore(key, Double.parseDouble(chatMessageReportDto.getScore()), Double.parseDouble(chatMessageReportDto.getScore()));

        for (Object object : rankObject != null ? rankObject : null) {
            targetRank = Long.valueOf(zSetOperations.rank(key, object));
        }

        // 총 카운트가 페이징 리밋보다 작으면 전체 가져오기
        if (lTotalCount <= searchDto.getRecordSize()) {
            return zSetOperations.range(key, 0, lTotalCount);
        } else {
            Long startRank;
            Long lastRank = lTotalCount;

            if ((targetRank - 50) < 1) {
                startRank = Long.valueOf(0);
            } else {
                startRank = targetRank - 50;
            }

            if ((targetRank + 50) < lTotalCount) {
                lastRank = targetRank + 50;
            }

            return zSetOperations.range(key, startRank, lastRank);
        }
    }

}
