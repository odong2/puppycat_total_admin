package com.architecture.admin.services.log;

import com.architecture.admin.models.daosub.log.MemberLoginLogDaoSub;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberLoginLogService extends BaseService {

    private final MemberLoginLogDaoSub memberLoginLogDaoSub;

    /**
     * 활동 내역 20개 리스트 by memberUuid
     *
     * @param memberDto memberUuid
     * @return list
     */
    public List<MemberDto> getLoginLog(MemberDto memberDto) {

        return memberLoginLogDaoSub.getLoginLog(memberDto);
    }
}
