package com.architecture.admin.services.log;

import com.architecture.admin.models.daosub.log.MemberAccessLogDaoSub;
import com.architecture.admin.models.dto.log.MemberAccessLogDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAccessLogService extends BaseService {

    private final MemberAccessLogDaoSub memberActionLogDaoSub;

    /**
     * 활동 내역 20개 리스트 by memberUuid
     *
     * @param memberAccessLogDto memberUuid
     * @return list
     */
    public List<MemberAccessLogDto> getMemberAccessLogLastTwentyCasesListByMemberUuid(MemberAccessLogDto memberAccessLogDto){

        return memberActionLogDaoSub.getMemberAccessLogLastTwentyCasesList(memberAccessLogDto);
    }
}
